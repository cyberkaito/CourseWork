function showLogin() {
    if ($(".loginup").is(":visible")) {
        $(".loginup").css("visibility", "visible");
        $("body").addClass("fixed");
    }
    $(".loginup form").attr("action", "Loginup/LogIn");
    $(".loginup form h3").text("Авторизация");
    $(".loginup form button").text("Войти");
    $(".loginup form div:nth-child(3)").css("display","none");
}
function showSignup() {
    if ($(".loginup").is(":visible")) {
        $(".loginup").css("visibility", "visible");
        $("body").addClass("fixed");
    }
    $(".loginup form").attr("action", "Loginup/LogUp");
    $(".loginup form h3").text("Регистрация");
    $(".loginup form button").text("Зарегистироваться");
    $(".loginup form div:nth-child(3)").css("display", "block");
}
function closeForm() {
    $(".loginup").css("visibility", "hidden");
    $("body").removeClass("fixed");
}
$(".form-check-input").mousedown(function () {
    if ($(".form-check-input").is(':checked')) {
        $(".form-check-input").attr("value", "true");
    } else {
        $(".form-check-input").attr("value", "false");
    }
})
var title = $('title').text();
switch (title) {
    case "Статья | Polyphonia":
        $("input[type='search']").attr("placeholder", "Поиск");
        $(".headerList li:nth-child(1)").addClass("active");
        break;
    case "Главная | Polyphonia":
        $("input[type='search']").attr("placeholder", "Поиск статей");
        $(".headerList li:nth-child(1)").addClass("active");
        $("input[type='search']").on("keyup", function () { 
            var value = $(this).val().toLowerCase(); 
            $(".col-md-4").filter(function () { 
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
            });
        });
        break;
    case "Каналы | Polyphonia":
        $("input[type='search']").attr("placeholder", "Поиск каналов");
        $(".headerList li:nth-child(2)").addClass("active");
        $("input[type='search']").on("keyup", function () {
            var value = $(this).val().toLowerCase();
            $(".row").filter(function () {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
            });
        });
        break;
    default:
        $(".headerList li:nth-child(2)").addClass("active");

}


// Пример стартового JavaScript для отключения отправки форм при наличии недопустимых полей
(function () {
    'use strict'

    // Получите все формы, к которым мы хотим применить пользовательские стили проверки Bootstrap
    var forms = document.querySelectorAll('.needs-validation')

    // Зацикливайтесь на них и предотвращайте отправку
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }

                form.classList.add('was-validated')
            }, false)
        })
})()