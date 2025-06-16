import axios from "@/axios"

export function uploadFile(formData) {
    // Make sure we're sending the FormData as is without any additional processing
    return axios.post("/admin/file/upload", formData)
}

