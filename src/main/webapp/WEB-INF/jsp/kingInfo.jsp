<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>天堂2m 打王</title>
</head>
<body>
<form action="http://localhost:8080/create" method="post" >
<%--    王的名稱: <input  type="text" name="name" id="name" value=""><br>--%>

    <b>請選擇王並輸入上次出現時間</b><br>

    王的名稱:
    <select name="id" id="id">
        <option value="1">佩爾利斯</option>
        <option value="2">巴實那</option>
        <option value="3">潘納洛德</option>
        <option value="4">采爾圖巴</option>
        <option value="5">坦佛斯特</option>
        <option value="6">魔圖拉</option>
        <option value="7">安庫拉</option>
        <option value="8">布賴卡</option>
        <option value="9">巨蟻女王</option>
        <option value="10">特論巴</option>
        <option value="11">雷比魯</option>
        <option value="12">史坦</option>
        <option value="13">被汙染的克魯瑪</option>
        <option value="14">提米特利斯</option>
        <option value="15">提米妮爾</option>
        <option value="16">塔金</option>
        <option value="17">克魯瑪</option>
        <option value="18">卡雷斯</option>
        <option value="19">貝希莫斯</option>
        <option value="20">塔拉金</option>
        <option value="21">核心基座</option>
        <option value="22">梅杜莎</option>
        <option value="23">卡坦</option>
        <option value="24">沙勒卡</option>
        <option value="25">凱索思</option>
        <option value="26">風王</option>
        <option value="27">賽魯</option>
        <option value="28">黑色蕾爾莉</option>
        <option value="29">薩班</option>
        <option value="30">瓦柏</option>
        <option value="31">猛龍獸</option>
        <option value="32">潘柴特</option>
        <option value="33">暗王</option>
        <option value="34">寇倫</option>
        <option value="35">水王</option>
        <option value="36">地王</option>
        <option value="37">奧爾芬</option>
    </select><br>

    新增王: <input  type="text" name="name" id="name" value=""><br>
    出現地點: <input  type="text" name="location" id="location" value=""><br>
    上次出現時間* (yyyy-mm-dd hh:mm:ss): <input  type="text" name="lastAppear" id="lastAppear" value="2021-08-26 00:00:00"> <br>
    出現週期(Min): <input  type="text" name="period" id="period" value=""><br>
    是否隨機出現:<br>
    <input type="radio" name="random" value="true"> 是
    <input type="radio" name="random" value="false"> 否<br>
<input type="submit"  name='submit' id="b" value="送出" >
</form>

</body>
</html>