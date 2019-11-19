$(function createDatepicker() {
    var currentYear = new Date().getFullYear();

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
            var img = document.createElement("IMG");
            img.src = "data:image/jpeg;base64," + data.photo;
            $("#photo").html(img).addClass("photo_borders");
        }
    });
}