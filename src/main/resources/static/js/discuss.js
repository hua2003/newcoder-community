function like(btn, entityType, entityId, entityUserId, postId) {
    $.post(
        CONTEXT_PATH + "/like",
        {
            "entityType": entityType,
            "entityId": entityId,
            "entityUserId": entityUserId,
            "postId": postId
        },
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus==1?'已赞':'赞');
            } else {
                alert(data.msg);
            }
        }
    )

    // $.ajax({
    //     url: CONTEXT_PATH + "/like",
    //     type: "POST",
    //     data: {
    //         "entityType": entityType,
    //         "entityId": entityId
    //     },
    //     dataType: "json",
    //     success: function (data) {
    //         data = $.parseJSON(data);
    //         if (data.code == 0) {
    //             $(btn).children("i").text(data.likeCount);
    //             $(btn).children("b").text(data.likeStatus==1?'已赞':'赞');
    //         } else {
    //             alert(data.msg);
    //         }
    //     }
    // })
}