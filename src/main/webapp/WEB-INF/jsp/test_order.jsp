<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LTS TEST</title>
</head>
<body>
<form action="http://secure.lingvist.com.tw/lts_j/api/createOrder" method="post" >
ENC_DATA_FIELD: <input  type="text" name="ENC_DATA_FIELD" id="ENC_DATA_FIELD" value="">  
MAC_DATA_FIELD: <input  type="text" name="MAC_DATA_FIELD" id="MAC_DATA_FIELD" value="">
<input type="submit"  name='submit' id="b" value="createOrder" >
</form>
<p>
<form action="http://secure.lingvist.com.tw/lts_j/api/cancelOrder" method="post" >
ENC_DATA_FIELD: <input  type="text" name="ENC_DATA_FIELD" id="ENC_DATA_FIELD" value="">  
MAC_DATA_FIELD: <input  type="text" name="MAC_DATA_FIELD" id="MAC_DATA_FIELD" value="">
<input type="submit"  name='submit' id="b" value=cancelOrder >
</form>
<p>
<form action="http://localhost:8080/api/queryOrder" method="post" >
ENC_DATA_FIELD: <input  type="text" name="ENC_DATA_FIELD" id="ENC_DATA_FIELD" value="">  
MAC_DATA_FIELD: <input  type="text" name="MAC_DATA_FIELD" id="MAC_DATA_FIELD" value="">
<input type="submit"  name='submit' id="b" value=queryOrder >
</form>
<p>
<form action="http://secure.lingvist.com.tw/lts_j/api/prepareOrder" method="post" >
ENC_DATA_FIELD: <input  type="text" name="ENC_DATA_FIELD" id="ENC_DATA_FIELD" value="">  
MAC_DATA_FIELD: <input  type="text" name="MAC_DATA_FIELD" id="MAC_DATA_FIELD" value="">
<input type="submit"  name='submit' id="b" value=prepareOrder >
</form>
<p>
<form action="http://secure.lingvist.com.tw/lts_j/api/completeOrder" method="post" >
ENC_DATA_FIELD: <input  type="text" name="ENC_DATA_FIELD" id="ENC_DATA_FIELD" value="">  
MAC_DATA_FIELD: <input  type="text" name="MAC_DATA_FIELD" id="MAC_DATA_FIELD" value="">
<input type="submit"  name='submit' id="b" value=completeOrder >
</form>

<p>
---------------------------------------------------------------------------
<p>
<p>
CREATE/PREPARE  ORDER
<p>
<form action="http://secure.lingvist.com.tw/lts_j/test/encrypt" method="post" >
PriceUuid: <input  type="text" name="priceUuid" id="priceUuid" value="5a9922ae-3ce8-48fd-85a8-67191cf1e2f5">  
TransactionId: <input  type="text" name="lsecOrderId" id="lsecOrderId" value="">
AccessToken: <input  type="text" name="accessToken" id="accessToken" value="">
MemberId: <input  type="text" name="memberid" id="memberid" value="tinaaaaaa">
<input type="submit"  name='submit2' id="b2" value="資料加密" >
</form>
<p>
<p>
CANCEL ORDER
<p>
<form action="http://secure.lingvist.com.tw/lts_j/test/encrypt" method="post" >
paymentUuid: <input  type="text" name="paymentUuid" id="paymentUuid" value="">  
MemberId: <input  type="text" name="memberid" id="memberid" value="tinaaaaaa">
<input type="submit"  name='submit2' id="b2" value="資料加密" >
</form>
<p>
<p>
COMPLETE ORDER
<p>
<form action="http://secure.lingvist.com.tw/lts_j/test/encrypt" method="post" >
orderUuid: <input  type="text" name="orderUuid" id="orderUuid" value="">  
MemberId: <input  type="text" name="memberid" id="memberid" value="tinaaaaaa">
<input type="submit"  name='submit2' id="b2" value="資料加密" >
</form>


</body>
</html>