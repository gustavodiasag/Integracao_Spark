package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Album {
	private int id;
	private String nome;
	private String artista;
	private int quantidade;
	private LocalDateTime dataFabricacao;	
	private String genero;
	
	public Album() {
		id = -1;
		nome = "";
		artista = "";
		quantidade = 0;
		dataFabricacao = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		genero = "";
	}

	public Album(int id, String nome, String artista, int quantidade, LocalDateTime fabricacao, String genero) {
		setId(id);
		setNome(nome);
		setArtista(artista);
		setQuantidade(quantidade);
		setDataFabricacao(fabricacao);
		setGenero(genero);
	}		
	
	public int getID() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome= nome;
	}

	public String getArtista() {
		return artista;
	}

	public void setArtista(String artista) {
		this.artista= artista;
	}

	public int getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}
	
	public LocalDateTime getDataFabricacao() {
		return dataFabricacao;
	}

	public void setDataFabricacao(LocalDateTime dataFabricacao) {
		// Pega a Data Atual
		LocalDateTime agora = LocalDateTime.now();
		// Garante que a data de fabricação não pode ser futura
		if (agora.compareTo(dataFabricacao) >= 0)
			this.dataFabricacao = dataFabricacao;
	}


	/**
	 * Método sobreposto da classe Object. É executado quando um objeto precisa
	 * ser exibido na forma de String.
	 */
	@Override
	public String toString() {
		return "Album: " + nome + "   Artista: " + artista + "   Quantidade.: " + quantidade + "   Fabricação: "
				+ dataFabricacao  + "   Genero: " + genero;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getID() == ((Album) obj).getID());
	}	
}