<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="/shoppingmall/css/style.css" />
<script type="text/javascript"
	src="http://code.jquery.com/jquery.min.js"></script>
<script type="text/javascript" src="/shoppingmall/qna/write.js"></script>

<c:if test="${empty sessionScope.id}">
	<meta http-equiv="Refresh" content="0;url=/shoppingmall/index.do">
</c:if>

<input type="hidden" id="qna_writer" value="${sessionScope.id }">
<input type="hidden" id="book_kind" value="${book_kind}">
<input type="hidden" id="book_id" value="${book_id }">
<input type="hidden" id="book_title" value="${book_title }">
<input type="hidden" id="qora" value="${qora}">

<div id="writeForm" class="box">
	<ul>
		<li>[${book_title }]에 대한 QnA
		<li><label for="content">내용</label> <textarea rows="13" cols="50"
				id="qnaCont"></textarea>
		<li class="label2">
			<button id="regist">등록</button>
			<button id="cancle">취소</button>
	</ul>
</div>