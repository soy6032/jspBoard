package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	//데이터베이스의 커넥션풀을 사용하도록 설정하는 메소드
	public void getCon() {
		
		try {
			
			Context initctx = new InitialContext();
			Context envctx = (Context)initctx.lookup("java:comp/env");
			DataSource ds = (DataSource) envctx.lookup("jdbc/pool");
			//datasource
			conn = ds.getConnection();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//하나의 새로운 게시글이 넘어와서 저장되는 메소드
	public void inserBoard(BoardBean bean) {
		
		getCon();
		//빈클래스에서 넘어오지 않았던 데이터들을 초기화해줌
		int ref = 0; //글그룹을 의미 = 쿼리를 실행시켜서 가장 큰 ref값을 가져온후 +1를 더해주면 됨
		int re_step = 1;
		int re_lever = 1;
		
		try {
			
			//가장큰 ref값을 가져오는 쿼리
			String refsql = "select max(ref) from board";
			//쿼리샐행 객체
			pstmt = conn.prepareStatement(refsql);
			rs = pstmt.executeQuery();
			if(rs.next()) {//쿼리의 결과값이 있다면
				ref = rs.getInt(1) + 1;// 최대값의 +1를 더해서 글그룹을 설정
			}
			
			//실제로 게시글 전체값을 테이블에 저장
			
			String sql = "insert into board values(board_seq.NEXTVAL,?,?,?,?,sysdate,?,?,?,0,?)";
			pstmt = conn.prepareStatement(sql);
			//?에 값을 맴핑
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSebject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step);
			pstmt.setInt(7, re_lever);
			pstmt.setString(8, bean.getContent());
			//쿼리를 실행
			pstmt.executeUpdate();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Vector<BoardBean> getAllBoard(){
		Vector<BoardBean> v = new Vector<>();
		getCon();
		try {
			String sql = "select * from board order by ref desc, re_step asc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			//데이터갯수가 몇개인지 몰르기에 반복문을 이용하여 데이터 추출
			while(rs.next()) {
				//데이터를 패키징(boaedBean클래스를 이용)
				BoardBean bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSebject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
				//패키징한 데이터를 백터(v)에 저장
				v.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}

}

















