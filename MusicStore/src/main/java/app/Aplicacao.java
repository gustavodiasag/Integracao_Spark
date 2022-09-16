package app;

import static spark.Spark.*;
import service.AlbumService;


public class Aplicacao {
	
	private static AlbumService albumService = new AlbumService();
	
    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        post("/album/insert", (request, response) -> albumService.insert(request, response));

        get("/album/:id", (request, response) -> albumService.get(request, response));
        
        get("/album/list/:orderby", (request, response) -> albumService.getAll(request, response));

        get("/album/update/:id", (request, response) -> albumService.getToUpdate(request, response));
        
        post("/album/update/:id", (request, response) -> albumService.update(request, response));
           
        get("/album/delete/:id", (request, response) -> albumService.delete(request, response));

             
    }
}