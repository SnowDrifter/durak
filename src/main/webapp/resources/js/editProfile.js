$(function createDatepicker() {
    const currentYear = new Date().getFullYear();

    $("#birthDate").datepicker({
        dateFormat: "dd mm yy",
        changeYear: true,
        yearRange: "1900:" + currentYear
    });
});

function loadPhoto(userId) {
    $.ajax({
        url: "/profile/" + userId + "/photo",
        success: function(data){
            const image = new Image();
            image.src = "data:image/jpeg;base64," + data.photo;
            $("#photo").html(image).addClass("photo_borders");
        }
    });
}