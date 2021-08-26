<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="https://code.jquery.com/jquery-3.3.1.min.js" type="text/javascript"></script>
<title>天堂2m 打王</title>
</head>
<body onload="init()">
<%--<center><form action="http://localhost:8080/create" method="post" >--%>
<center><form action="https://line-bot-lineageking.herokuapp.com/create" method="post" >

    <font color="blue"><b>請選擇王並輸入上次出現時間</b></font><br>
    <font color="#808080">(如果要新增王的資訊請填寫所有資料)</font><br><br><br>

    王的名稱:
    <select name="kingId" id="kingId">
        <option value="">請選擇</option>
    </select><br>

    新增王: <input  type="text" name="name" id="name" value=""><br>
    出現地點: <input  type="text" name="location" id="location" value=""><br>
    上次出現時間<font color="red">**</font> (yyyy-mm-dd hh:mm:ss): <input  type="text" name="lastAppear" id="lastAppear" value="2021-08-26 00:00:00" required> <br>
    出現週期(Min): <input  type="text" name="period" id="period" value=""><br>
    是否隨機出現:<br>
    <input type="radio" name="random" value="true"> 是
    <input type="radio" name="random" value="false"> 否<br>
<input type="submit"  name='submit' id="b" value="送出" >
</form></center>
<script type="text/javascript">
    function init() {

        var kingId = document.getElementById('kingId');
        $.ajax({
            async: false,
            type :"GET",
            url  : "/kinglist",
            contentType: "application/json",
            dataType: "json",
            success : function(result) {
                for (var i = 0; i < result.length; i++) {
                    var opt = document.createElement('option');
                    opt.text = result[i].name;
                    opt.value = i+1;
                    kingId.appendChild(opt);
                }
            },
            error: function(err){
                console.log(err);
            }
        });
    }
</script>
</body>
</html>