$(function() {
    $("#topBtn").click(setTop);
    $("#wonderfulBtn").click(setWonderful);
    $("#deleteBtn").click(setDelete);
});

function setTop() {
    $.post(
        CONTEXT_PATH + "/discuss/top",
        {
            "id": $("#postId").val()
        },
        function(data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                $("#topBtn").attr("disabled", "disabled");
            } else {
                alert(data.msg);
            }
        }
    )
}

function setWonderful() {
    $.post(
        CONTEXT_PATH + "/discuss/wonderful",
        {
            "id": $("#postId").val()
        },
        function(data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                $("#wonderfulBtn").attr("disabled", "disabled");
            } else {
                alert(data.msg);
            }
        }
    )
}
function setDelete() {
    $.post(
        CONTEXT_PATH + "/discuss/delete",
        {
            "id": $("#postId").val()
        },
        function(data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                location.href = CONTEXT_PATH + "/index";
            } else {
                alert(data.msg);
            }
        }
    )
}

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