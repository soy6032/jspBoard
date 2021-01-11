<%@page import="model.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		
		<%
			request.setCharacterEncoding("UTF-8");
		%>
		
		<!-- 작성한 게시글 데이터를 한번에 읽어드림  -->
		
		<jsp:useBean id="boardbean" class="model.BoardBean">
			<jsp:setProperty name="boardbean" property="*" />
		</jsp:useBean>
		
		<%
		
		//데이터 베이스쪽으로 빈클래스를 넘겨줌
		BoardDAO bdao = new BoardDAO();
		
		bdao.inserBoard(boardbean);
		
		//게시글 저장후 전체게시글 보기
		response.sendRedirect("BoardList.jsp");
		
		%>
		
		
	</body>
</html>