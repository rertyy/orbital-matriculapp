import com.google.gson.annotations.SerializedName


// TODO add postId and categoryId to Post
data class Thread(
    @SerializedName("thread_id") val threadId: Int = -1,
    @SerializedName("thread_name") val threadName: String = "",
    @SerializedName("thread_body") val threadBody: String = "",
    @SerializedName("thread_created_by") val createdBy: String,
    @SerializedName("thread_created_at") val createdAt: String = "",
    @SerializedName("user_id") val userId: Int,
    @SerializedName("username") val username: String
)

data class Reply(
    @SerializedName("reply_id") val replyId: Int = -1,
    @SerializedName("reply_body") val replyBody: String = "",
    @SerializedName("reply_created_at") val replyCreatedAt: String = "",
    @SerializedName("user_id") val userId: Int,
    @SerializedName("username") val username: String
)
