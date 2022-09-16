package dao;

import model.Album;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class AlbumDAO extends DAO {	
	public AlbumDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert(Album album) {
		boolean status = false;
		try {
			System.out.println(album.getNome() + album.getArtista() + album.getGenero());
			String sql = "INSERT INTO album (nome, artista, quantidade, datafabricacao, genero) "
		               + "VALUES ('" + album.getNome() + "', '"
		               + album.getArtista() + "', " + album.getQuantidade() + ", ?, '" + album.getGenero() + "');";
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(album.getDataFabricacao()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public Album get(int id) {
		Album album = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM album WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 album = new Album(rs.getInt("id"), rs.getString("nome"), rs.getString("artista"), 
	                				   rs.getInt("quantidade"), 
	        			               rs.getTimestamp("datafabricacao").toLocalDateTime(),
	        			               rs.getString("genero"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return album;
	}
	
	
	public List<Album> get() {
		return get("");
	}

	
	public List<Album> getOrderByID() {
		return get("id");		
	}
	
	
	public List<Album> getOrderByNome() {
		return get("nome");		
	}
	
	
	public List<Album> getOrderByArtista() {
		return get("artista");		
	}
	
	
	private List<Album> get(String orderBy) {
		List<Album> albums = new ArrayList<Album>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM album" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Album p = new Album(rs.getInt("id"), rs.getString("nome"), rs.getString("artista"), 
	        			                rs.getInt("quantidade"),
	        			                rs.getTimestamp("datafabricacao").toLocalDateTime(),
	        			                rs.getString("genero"));
	            albums.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return albums;
	}
	
	
	public boolean update(Album album) {
		boolean status = false;
		try {  
			String sql = "UPDATE album SET nome = '" + album.getNome() + "', "
					   + "artista = '" + album.getArtista() + "', " 
					   + "quantidade = " + album.getQuantidade() + ","
					   + "datafabricacao = ?, " 
					   + "genero = '" + album.getGenero() + "' WHERE id = " + album.getID();
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(album.getDataFabricacao()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	public boolean delete(int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM album WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}