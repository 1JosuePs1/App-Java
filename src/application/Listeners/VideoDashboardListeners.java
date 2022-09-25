package application.Listeners;

import application.Model.Video;
import java.io.IOException;
/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Listener de Videos en el Dashboard
 */


public interface VideoDashboardListeners {
    /**Este Listener procesa o "escucha" el evento del Video al dar click on play
     * @param video
     * */
    public void onClickPlay(Video video) throws IOException;
}
