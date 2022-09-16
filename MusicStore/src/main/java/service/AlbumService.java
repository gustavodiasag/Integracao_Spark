package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.AlbumDAO;
import model.Album;
import spark.Request;
import spark.Response;


public class AlbumService {

	private AlbumDAO albumDAO = new AlbumDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_DESCRICAO = 2;
	private final int FORM_ORDERBY_PRECO = 3;
	
	
	public AlbumService() {
		makeForm();
	}

	public void makeForm() {
		makeForm(FORM_INSERT, new Album(), FORM_ORDERBY_DESCRICAO);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Album(), orderBy);
	}

	
	public void makeForm(int tipo, Album album, int orderBy) {
		String nomeArquivo = "form.html";
		form = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	form += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umProduto = "";
		if(tipo != FORM_INSERT) {
			umProduto += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umProduto += "\t\t<tr>";
			umProduto += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/album/list/1\">Novo Album</a></b></font></td>";
			umProduto += "\t\t</tr>";
			umProduto += "\t</table>";
			umProduto += "\t<br>";			
		}
		
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/album/";
			String name, descricao, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "insert";
				name = "Inserir Album";
				descricao = "In rainbows, Hounds of Love, ...";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + album.getID();
				name = "Atualizar Album (ID " + album.getID() + ")";
				descricao = album.getNome();
				buttonLabel = "Atualizar";
			}
			umProduto += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umProduto += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umProduto += "\t\t<tr>";
			umProduto += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
			umProduto += "\t\t</tr>";
			umProduto += "\t\t<tr>";
			umProduto += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umProduto += "\t\t</tr>";
			umProduto += "\t\t<tr>";
			umProduto += "\t\t\t<td>&nbsp;Nome: <input class=\"input--register\" type=\"text\" name=\"nome\" value=\""+ descricao +"\"></td>";
			umProduto += "\t\t\t<td>Artista: <input class=\"input--register\" type=\"text\" name=\"artista\" value=\""+ album.getArtista() +"\"></td>";
			umProduto += "\t\t\t<td>Quantidade: <input class=\"input--register\" type=\"text\" name=\"quantidade\" value=\""+ album.getQuantidade() +"\"></td>";
			umProduto += "\t\t</tr>";
			umProduto += "\t\t<tr>";
			umProduto += "\t\t\t<td>&nbsp;Data de fabricação: <input class=\"input--register\" type=\"text\" name=\"dataFabricacao\" value=\""+ album.getDataFabricacao().toString() + "\"></td>";
			umProduto += "\t\t\t<td>Genero: <input class=\"input--register\" type=\"text\" name=\"genero\" value=\""+ album.getGenero() + "\"></td>";
			umProduto += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
			umProduto += "\t\t</tr>";
			umProduto += "\t</table>";
			umProduto += "\t</form>";		
		} else if (tipo == FORM_DETAIL){
			umProduto += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umProduto += "\t\t<tr>";
			umProduto += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Album (ID " + album.getID() + ")</b></font></td>";
			umProduto += "\t\t</tr>";
			umProduto += "\t\t<tr>";
			umProduto += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umProduto += "\t\t</tr>";
			umProduto += "\t\t<tr>";
			umProduto += "\t\t\t<td>&nbsp;Album: "+ album.getNome() +"</td>";
			umProduto += "\t\t\t<td>Artista: "+ album.getArtista() +"</td>";
			umProduto += "\t\t\t<td>Quantidade: "+ album.getQuantidade() +"</td>";
			umProduto += "\t\t</tr>";
			umProduto += "\t\t<tr>";
			umProduto += "\t\t\t<td>&nbsp;Data de fabricação: "+ album.getDataFabricacao().toString() + "</td>";
			umProduto += "\t\t\t<td>Genero: "+ album.getGenero() + "</td>";
			umProduto += "\t\t\t<td>&nbsp;</td>";
			umProduto += "\t\t</tr>";
			umProduto += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-PRODUTO>", umProduto);
		
		String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Albums</b></font></td></tr>\n" +
				"\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td><a href=\"/album/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
        		"\t<td><a href=\"/album/list/" + FORM_ORDERBY_DESCRICAO + "\"><b>Nome</b></a></td>\n" +
        		"\t<td><a href=\"/album/list/" + FORM_ORDERBY_PRECO + "\"><b>Artista</b></a></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Album> albums;
		if (orderBy == FORM_ORDERBY_ID) {                 	albums = albumDAO.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_DESCRICAO) {		albums = albumDAO.getOrderByNome();
		} else if (orderBy == FORM_ORDERBY_PRECO) {			albums = albumDAO.getOrderByArtista();
		} else {											albums = albumDAO.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Album a : albums) {
			bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td>" + a.getID() + "</td>\n" +
            		  "\t<td>" + a.getNome() + "</td>\n" +
            		  "\t<td>" + a.getArtista() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/album/" + a.getID() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/album/update/" + a.getID() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteAlbum('" + a.getID() + "', '" + a.getNome() + "', '" + a.getArtista() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		form = form.replaceFirst("<LISTAR-PRODUTO>", list);				
	}
	
	
	public Object insert(Request request, Response response) {
		String nome = request.queryParams("nome");
		String artista = request.queryParams("artista");
		int quantidade = Integer.parseInt(request.queryParams("quantidade"));
		LocalDateTime dataFabricacao = LocalDateTime.parse(request.queryParams("dataFabricacao"));
		String genero = request.queryParams("genero");
		
		String resp = "";
		
		Album album = new Album(-1, nome, artista, quantidade, dataFabricacao, genero);
		
		if(albumDAO.insert(album) == true) {
            resp = "Album (" + nome + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "Album (" + nome + ") não inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Album album = (Album) albumDAO.get(id);
		
		if (album != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, album, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Album " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Album album = (Album) albumDAO.get(id);
		
		if (album != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, album, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Album " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Album album = albumDAO.get(id);
        String resp = "";       

        if (album != null) {
        	album.setNome(request.queryParams("nome"));
        	album.setArtista(request.queryParams("artista"));
        	album.setQuantidade(Integer.parseInt(request.queryParams("quantidade")));
        	album.setDataFabricacao(LocalDateTime.parse(request.queryParams("dataFabricacao")));
        	album.setGenero(request.queryParams("genero"));
        	albumDAO.update(album);
        	response.status(200); // success
            resp = "Album (ID " + album.getID() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Album (ID " + album.getId() + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Album album = albumDAO.get(id);
        String resp = "";       

        if (album != null) {
            albumDAO.delete(id);
            response.status(200); // success
            resp = "Album (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Album (" + id + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}