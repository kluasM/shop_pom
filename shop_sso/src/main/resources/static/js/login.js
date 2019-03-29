$(function () {
    $.ajax({
        url:"http://localhost:8086/sso/islogin",
        success:function (data) {
            // alert(JSON.stringify(data));
            if (data!=null){
                $("#pid").html(data.name+"您好，欢迎来到<b>ShopCZ商城</b><a href='http://localhost:8086/sso/logout'>注销</a>");
            }else {
                $("#pid").html("[<a href=\"javascript:login();\">登录</a>][<a href=\"\">注册</a>]")
            }
        },
        //jsonp解决跨域问题
        dataType:"jsonp",
        jsonpCallback:"islogin"
    });

});
function login() {
    var returnURL=location.href;
    returnURL=encodeURI(returnURL,"utf-8");
    //多个参数的解决方法
    returnURL=returnURL.replace("&","%26");
    alert("当前浏览器的地址："+location.href);
    location.href="http://localhost:8086/sso/tologin?returnURL=" + returnURL;
}