$(function createDatepicker() {
    const currentYear = new Date().getFullYear();

    $("#birthDate").datepicker({
        dateFormat: "dd.mm.yy",
        changeYear: true,
        yearRange: "1900:" + currentYear
    });
});

function uploadPhoto(userId) {
    $("#upload_photo_error").css("display", "none");

    const formData = new FormData();
    const photo = $("input[name='photo']")[0].files[0];
    formData.append("file", photo);

    $.ajax({
        url: `/profile/${userId}/photo/upload`,
        type: "post",
        data: formData,
        contentType: false,
        processData: false,
        complete: function (response) {
            if (response.status === 200) {
                displayPhoto(photo)
            } else {
                $("#upload_photo_error").css("display", "inline");
            }
        },
    });

    return false;
}

const displayPhoto = photo => new Promise(() => {
    const reader = new FileReader();
    reader.readAsDataURL(photo);
    reader.onload = function () {
        const image = new Image();
        image.src = reader.result;
        $("#photo").html(image).addClass("photo_borders");
    };
});

function loadPhoto(userId) {
    $.ajax({
        url: "/profile/" + userId + "/photo",
        success: function (data) {
            const image = new Image();
            image.src = "data:image/jpeg;base64," + data.photo;
            $("#photo").html(image).addClass("photo_borders");
        }
    });
}