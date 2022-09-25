module Proyecto {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.media;
    requires javafx.web;
    requires java.sql;
    requires mysql.connector.java;

    opens application;

    //Models
    opens application.Model;

    //Controllers
    opens application.Controller.Video;
    opens application.Controller.Users;
    opens application.Controller.Dashboard;
    opens application.Controller.Playlist;
    opens application.Controller.Templates;
    opens application.Controller.Categoria;
    opens application.Controller.Socket;

    //Vistas
    opens application.View.Video;
    opens application.View.Users;
    opens application.View.Dashboard;
    opens application.View.Playlist;
    opens application.View.Templates;
    opens application.View.Categoria;
    opens application.View.Socket;

    //DAO
    opens application.DAO;
    opens application.DAO.Categoria;
    opens application.DAO.Playlist;
    opens application.DAO.Users;
    opens application.DAO.Video;
}