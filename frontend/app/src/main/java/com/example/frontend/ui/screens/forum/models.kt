import com.google.gson.annotations.SerializedName


// TODO add postId and categoryId to Post
data class Thread(
    val title: String = "",
    val body: String = "",
    @SerializedName("thread_id") val threadId: Int = -1,
    //@SerializedName("category_name") val categoryName: String,
    //@SerializedName("created_by") val createdBy: Int,
    //@SerializedName("created_by_name") val createdByName: String,

//    @SerializedName("created_at") val createdAt: OffsetDateTime,
//    @SerializedName("last_updated") val lastUpdated: OffsetDateTime
)

data class Reply(
    @SerializedName("reply_id") val replyId: Int = -1,
    val body: String = "",
    @SerializedName("thread_id") val threadId: Int = -1
)
