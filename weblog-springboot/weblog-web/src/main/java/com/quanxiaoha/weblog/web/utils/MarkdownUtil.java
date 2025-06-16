package com.quanxiaoha.weblog.web.utils;

import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.block.NodePostProcessor;
import com.vladsch.flexmark.parser.block.NodePostProcessorFactory;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.NodeTracker;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.html.MutableAttributes;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import com.vladsch.flexmark.util.sequence.CharSubSequence;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/6
 * @time 下午12:32
 * @discription markdown 工具类
 **/
@Slf4j
@Component
public class MarkdownUtil {

    private static MinioUrlConverter minioUrlConverter;

    @Autowired
    public void setMinioUrlConverter(MinioUrlConverter minioUrlConverter) {
        MarkdownUtil.minioUrlConverter = minioUrlConverter;
    }

    private final static Parser parser;
    private final static HtmlRenderer renderer;

    private static final String SITE_NAME = "www.quanxiaoha.com";

    static {
        // 定制 markdown 选项
        MutableDataSet options = new MutableDataSet();

        // set optional extensions
        options.set(Parser.EXTENSIONS, Arrays.asList(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                NofollowExtension.create(),
                NodeInsertingPostProcessorExtension.create()));

        // convert soft-breaks to hard breaks
        // options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }

    static class NodeInsertingPostProcessor extends NodePostProcessor {
        private static class NodeInsertingFactory extends NodePostProcessorFactory {
            NodeInsertingFactory(DataHolder options) {
                super(false);
                addNodes(Image.class);
                // addNodes(ImageRef.class);
                addNodes(Link.class);
            }

            @NotNull
            @Override
            public NodePostProcessor apply(@NotNull Document document) {
                return new NodeInsertingPostProcessor();
            }
        }

        public static NodePostProcessorFactory Factory(DataHolder options) {
            return new NodeInsertingFactory(options);
        }

        @Override
        public void process(@NotNull NodeTracker state, @NotNull Node node) {
            BasedSequence paragraphText = BasedSequence.NULL;
            if (node instanceof Image) { // ![bar](http://example.com)
                Image image = (Image) node;
                paragraphText = image.getText();

                // 转换图片URL
                String imageUrl = image.getUrl().toString();
                if (imageUrl != null && !imageUrl.isEmpty() && minioUrlConverter != null) {
                    try {
                        String convertedUrl = minioUrlConverter.convert(imageUrl);
                        if (!imageUrl.equals(convertedUrl)) {
                            log.info("转换Markdown中的图片URL: {} -> {}", imageUrl, convertedUrl);
                            // 替换图片URL
                            image.setUrl(CharSubSequence.of(convertedUrl));
                        }
                    } catch (Exception e) {
                        log.error("转换Markdown中的图片URL失败: {}", imageUrl, e);
                    }
                }

                if (!paragraphText.isBlank()) {
                    // create a text element to hold the text
                    String html = String.format("<span class=\"image-caption\">%s</span>", paragraphText);
                    HtmlInline inline = new HtmlInline(CharSubSequence.of(html));
                    node.insertAfter(inline);
                    state.nodeAdded(inline);
                }
            } else if (node instanceof Link) {
                Node paragraphParent = node.getAncestorOfType(Paragraph.class);

                if (paragraphParent != null) {
                    String html = "<span><svg xmlns=\"http://www.w3.org/2000/svg\" style=\"color: #aaa;\" aria-hidden=\"true\" focusable=\"false\" x=\"0px\" y=\"0px\" viewBox=\"0 0 100 100\" width=\"15\" height=\"15\" class=\"icon outbound\"><path fill=\"currentColor\" d=\"M18.8,85.1h56l0,0c2.2,0,4-1.8,4-4v-32h-8v28h-48v-48h28v-8h-32l0,0c-2.2,0-4,1.8-4,4v56C14.8,83.3,16.6,85.1,18.8,85.1z\"></path> <polygon fill=\"currentColor\" points=\"45.7,48.7 51.3,54.3 77.2,28.5 77.2,37.2 85.2,37.2 85.2,14.9 62.8,14.9 62.8,22.9 71.5,22.9\"></polygon></svg> <span class=\"sr-only\"></span></span>";
                    HtmlInline inline = new HtmlInline(CharSubSequence.of(html));
                    node.insertAfter(inline);
                    state.nodeAdded(inline);
                }
            }
        }
    }

    static class NodeInsertingPostProcessorExtension implements Parser.ParserExtension {
        private NodeInsertingPostProcessorExtension() {
        }

        @Override
        public void parserOptions(MutableDataHolder options) {
        }

        @Override
        public void extend(Parser.Builder parserBuilder) {
            parserBuilder.postProcessorFactory(NodeInsertingPostProcessor.Factory(parserBuilder));
        }

        public static NodeInsertingPostProcessorExtension create() {
            return new NodeInsertingPostProcessorExtension();
        }
    }

    static class NofollowExtension implements HtmlRenderer.HtmlRendererExtension {

        @Override
        public void rendererOptions(@NotNull MutableDataHolder mutableDataHolder) {

        }

        @Override
        public void extend(final HtmlRenderer.Builder rendererBuilder, final String rendererType) {
            rendererBuilder.attributeProviderFactory(NofollowAttributeProvider.Factory());
        }

        static NofollowExtension create() {
            return new NofollowExtension();
        }
    }

    static class NofollowAttributeProvider implements AttributeProvider {

        static AttributeProviderFactory Factory() {
            return new IndependentAttributeProviderFactory() {
                @NotNull
                @Override
                public AttributeProvider apply(@NotNull LinkResolverContext context) {
                    return new NofollowAttributeProvider();
                }
            };
        }

        @Override
        public void setAttributes(@NotNull Node node,
                @NotNull AttributablePart attributablePart,
                @NotNull MutableAttributes mutableAttributes) {
            if ((node instanceof Link || node instanceof AutoLink)
                    && (attributablePart == AttributablePart.LINK)) {

                mutableAttributes.replaceValue("target", "_blank");
                String href = mutableAttributes.getValue("href");
                // 对于 md 文档中不包含本站域名的，设置 nofollow
                if (!href.contains(SITE_NAME)) {
                    // Put info in custom attribute instead
                    mutableAttributes.replaceValue("rel", "nofollow");
                }
            } else if (node instanceof Image && attributablePart == AttributablePart.LINK) {
                // 为图片链接应用URL转换
                String src = mutableAttributes.getValue("src");
                if (src != null && !src.isEmpty() && minioUrlConverter != null) {
                    try {
                        String convertedSrc = minioUrlConverter.convert(src);
                        if (!src.equals(convertedSrc)) {
                            log.info("转换HTML渲染时的图片URL: {} -> {}", src, convertedSrc);
                            mutableAttributes.replaceValue("src", convertedSrc);
                        }
                    } catch (Exception e) {
                        log.error("转换HTML渲染时的图片URL失败: {}", src, e);
                    }
                }
            }
        }
    }

    /**
     * 解析 markdown 为 html
     * 
     * @param mdStr
     * @return
     */
    public static String parse2Html(String mdStr) {
        try {
            Node document = parser.parse(mdStr);
            return renderer.render(document);
        } catch (Exception e) {
            log.error("markdown parse to html exception: ", e);
            return null;
        }
    }

    /**
     * 带URL转换的markdown解析
     * 通过手动处理HTML文本来转换图片URL
     * 该方法是为了兼容MinioUrlConverter未被注入的情况
     */
    public static String parse2HtmlWithUrlConverting(String mdStr, MinioUrlConverter urlConverter) {
        try {
            if (minioUrlConverter == null && urlConverter != null) {
                // 临时设置，用于本次转换
                minioUrlConverter = urlConverter;
            }

            String html = parse2Html(mdStr);

            // 即使在解析时转换了URL，这里再做一次简单的HTML文本替换确保所有URL都被转换
            if (html != null && !html.isEmpty() && urlConverter != null) {
                // 替换img标签中的src属性
                html = replaceImgSrc(html, urlConverter);
            }

            return html;
        } catch (Exception e) {
            log.error("parse markdown with URL converting failed: ", e);
            return parse2Html(mdStr); // 失败回退到普通解析
        }
    }

    /**
     * 替换HTML中的图片URL
     */
    private static String replaceImgSrc(String html, MinioUrlConverter urlConverter) {
        if (html == null || urlConverter == null) {
            return html;
        }

        StringBuilder result = new StringBuilder();
        int index = 0;

        try {
            // 简单的HTML解析和替换
            while (index < html.length()) {
                int imgTagStart = html.indexOf("<img", index);
                if (imgTagStart == -1) {
                    result.append(html.substring(index));
                    break;
                }

                result.append(html.substring(index, imgTagStart));

                int srcStart = html.indexOf("src=\"", imgTagStart);
                if (srcStart == -1) {
                    result.append(html.substring(imgTagStart));
                    break;
                }

                srcStart += 5; // 跳过"src=""
                int srcEnd = html.indexOf("\"", srcStart);
                if (srcEnd == -1) {
                    result.append(html.substring(imgTagStart));
                    break;
                }

                String srcUrl = html.substring(srcStart, srcEnd);
                // 调用URL转换
                String convertedUrl = urlConverter.convert(srcUrl);

                // 只有URL变化时才记录日志，减少日志噪音
                if (!srcUrl.equals(convertedUrl)) {
                    log.info("HTML图片URL转换: {} -> {}", srcUrl, convertedUrl);
                }

                result.append(html.substring(imgTagStart, srcStart));
                result.append(convertedUrl);

                // 继续处理下一个部分
                index = srcEnd;
            }

            return result.toString();
        } catch (Exception e) {
            log.error("替换HTML中的图片URL失败: {}", e.getMessage(), e);
            // 失败时返回原始HTML
            return html;
        }
    }

    public static void main(String[] args) {
        // System.out.println(parse2Html("| fdsf | fdsf |\n" +
        // "| ------------ | ------------ |\n" +
        // "| fdsf |fsdf |\n" +
        // "|fsdf | fdsf |\n" +
        // "\n" +
        // ":warnings:"));

        System.out.println(parse2Html("## 一、Spring Boot 是什么ss\n" +
                "\n" +
                "以下截图自 [Spring Boot 官方文档](https://spring.io/projects/spring-boot/)：\n" +
                "\n" +
                "![什么是Spring Boot](https://exception-image-bucket.oss-cn-hangzhou.aliyuncs.com/155523379091222 \"什么是Spring Boot\")"));

        // StringBuilder html = new StringBuilder("");
        // String markdown = MarkdownUtil.parse2Html(html.toString());
        //
        //// System.out.println("HTML:");
        //// System.out.println(html);
        //
        // System.out.println("\nMarkdown:");
        // System.out.println(markdown);
    }

}
