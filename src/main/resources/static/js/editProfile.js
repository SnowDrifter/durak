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

    const photo = $("input[name='photo']")[0].files[0];

    const formData = new FormData();
    formData.append("file", photo);
    formData.append("userId", userId);

    $.ajax({
        url: "/user/photo/upload",
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
        image.className = "photo_borders";
        $("#photo").html(image);
    };
});

function loadPhoto(photoId) {
    const image = new Image();
    image.src = "/media/photo?photoId=" + photoId;
    image.className = "photo_borders";
    $("#photo").html(image);
}