<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
	
	<h2>게시글쓰기</h2>
	
	
	<table width="600" border="1" bgcolor="lightgray">
	<form action="BoardWriteProc.jsp" method="post">
		<tr height="40">
			<td align="center" width="150">작성자</td>
			<td width="450">&nbsp;<input type="text" name="writer" size="60"></td>
		</tr>
		<tr height="40">
			<td align="center" width="150">제목</td>
			<td width="450">&nbsp;<input type="text" name="sebject" size="60"></td>
		</tr>
		<tr height="40">
			<td align="center" width="150">이메일</td>
			<td width="450">&nbsp;<input type="email" name="email" size="60"></td>
		</tr>
		<tr height="40">
			<td align="center" width="150">비밀번호</td>
			<td width="450">&nbsp;<input type="password" name="password" size="60"></td>
		</tr>
		<tr height="40">
			<td align="center" width="150">글내용</td>
			<td width="450"><textarea rows="15" cols="60" name="content"></textarea></td>
		</tr>
		<tr height="40">
			<td align="center"colspan="2">
				<input type="submit" value="글쓰기">&nbsp;&nbsp;
				<input type="reset" value="다시작성">&nbsp;&nbsp;
		</form>
				<button onclick="location.href='BoardList.jsp'">목록</button>
			</td>
			
		</tr>
		
		
	</table>
	
	
		
	</body>
</html>