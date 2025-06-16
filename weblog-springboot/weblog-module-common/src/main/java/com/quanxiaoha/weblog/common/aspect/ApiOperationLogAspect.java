
package com.quanxiaoha.weblog.common.aspect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quanxiaoha.weblog.common.domain.dos.VisitorRecordDO;
import com.quanxiaoha.weblog.common.domain.mapper.VisitorMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static com.quanxiaoha.weblog.common.utils.AgentRegionUtils.getIpAddress;
import static com.quanxiaoha.weblog.common.utils.AgentRegionUtils.getIpRegion;


/**
 * API操作日志切面类
 * 用于记录接口访问日志、统计接口耗时，并实现访客IP跟踪功能
 */
@Aspect
@Component
@Slf4j
@EnableScheduling
public class ApiOperationLogAspect {

    /**
     * 系统换行符
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * 线程安全的HashMap，用于存储当天访问的访客IP及其归属地
     * key: IP地址, value: IP归属地
     */
    private static final ConcurrentHashMap<String, String> AGENT_IP = new ConcurrentHashMap<>(8);

    /**
     * 定时任务执行表达式：每天凌晨00:00:01执行
     * 用于清空当天访客IP记录，开始新一天的统计
     */
    private static final String TASK_SCHEDULE = "1 0 0 * * ?" ;

    /**
     * ip2region.xdb数据库文件路径
     * 通过配置文件注入，用于IP地址归属地查询
     */
    @Value("${xdb.profile}")
    private String xdbPath;

    /**
     * 访客记录数据访问接口
     * 用于将新访客信息保存到数据库
     */
    @Resource
    private VisitorMapper visitorMapper;

    /**
     * 定义切点：所有添加了@ApiOperationLog注解的方法
     */
    @Pointcut("@annotation(com.quanxiaoha.weblog.common.aspect.ApiOperationLog)")
    public void apiOperationLog() {
    }

    /**
     * 前置通知：在目标方法执行前执行
     * 记录请求参数、打印请求日志，并处理新访客IP记录
     * @param joinPoint 连接点对象，包含目标方法信息
     * @throws Throwable 处理过程中可能抛出的异常
     */
    @Before("apiOperationLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取日志注解的描述信息
        String methodDescription = getAspectLogDescription(joinPoint);

        // 打印请求相关参数
        log.info("========== 请求开始: [{}], 入参: {} =================================== ", methodDescription, toJson(joinPoint));
        log.warn("请求的类: {}, 方法: {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

        // 根据request 获取ip地址并查询归属地
        String ipAddress = getIpAddress(request);
        if (!AGENT_IP.containsKey(ipAddress)) {

            VisitorRecordDO visitorRecordDO = new VisitorRecordDO();
            String region = getIpRegion(ipAddress, xdbPath);
            // 将新的访客IP和归属地信息放入map
            AGENT_IP.put(ipAddress, region);

            Date date = new Date();
            visitorRecordDO.setVisitor("agent");
            visitorRecordDO.setIpAddress(ipAddress);
            visitorRecordDO.setIpRegion(region);
            visitorRecordDO.setVisitTime(date);
            // todo 是否邮箱通知，默认为0不通知
            visitorRecordDO.setIsNotify(0);
            visitorMapper.insert(visitorRecordDO);
            log.info("ipAddress: {}, Region: {}", ipAddress, region);

        }
    }

    /**
     * 定时任务：每天凌晨清空访客IP记录
     * 确保AGENT_IP只保存当天的访客信息
     */
    @Scheduled(cron = TASK_SCHEDULE)
    private void scheduledClearTask() {
        AGENT_IP.clear();
    }


    /**
     * 后置通知：在目标方法执行后执行
     * 当前未实现具体逻辑
     * @throws Throwable 可能抛出的异常
     */
    @After("apiOperationLog()")
    public void doAfter() throws Throwable {
        // nothing
    }

    /**
     * 环绕通知：包围目标方法执行
     * 用于计算接口执行耗时并记录返回结果
     * @param proceedingJoinPoint 可执行的连接点对象
     * @return 目标方法的返回结果
     * @throws Throwable 目标方法执行过程中可能抛出的异常
     */
    @Around("apiOperationLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 耗时
        long timeConsuming = System.currentTimeMillis() - startTime;
        String methodDescription = getAspectLogDescription(proceedingJoinPoint);

        log.info("========== 请求结束: [{}], 耗时: {}ms, 出参: {} =================================== {}", methodDescription, timeConsuming, new ObjectMapper().writeValueAsString(result), LINE_SEPARATOR);
        return result;
    }

    /**
     * 获取@ApiOperationLog注解的描述信息
     * @param joinPoint 连接点对象
     * @return 注解中的描述文本
     * @throws Exception 反射获取方法信息时可能抛出的异常
     */
    public String getAspectLogDescription(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder("");
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description.append(method.getAnnotation(ApiOperationLog.class).description());
                    break;
                }
            }
        }
        return description.toString();
    }

    /**
     * 将连接点的参数转换为JSON字符串
     * @param joinPoint 连接点对象
     * @return 参数的JSON字符串表示
     * @throws JsonProcessingException JSON序列化异常
     */
    private String toJson(JoinPoint joinPoint) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(joinPoint.getArgs());
    }

}
