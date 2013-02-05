/*
 * MediathekView
 * Copyright (C) 2008 W. Xaver
 * W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package mediathek.controller.io.starter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mediathek.daten.DatenDownload;
import mediathek.tool.Log;

class RuntimeExec {

    private static final int INPUT = 1;
    private static final int ERROR = 2;
    private String prog;
    Thread clearIn;
    Thread clearOut;
    private Process process = null;
    Start s;
    private static int procnr = 0; //TH
    private Pattern patternFlvstreamer = Pattern.compile("([0-9.]*%)");
    private Pattern patternFfmpeg = Pattern.compile("(?<=Duration: )[^,]*");
    private Pattern patternZeit = Pattern.compile("(?<=time=)[\\d.]+");
    private double totalSecs = 0;
    private String zeit, prozent;

    /**
     * Neue Klasse instanzieren
     */
    public RuntimeExec(Start st) {
        s = st;
        prog = s.datenDownload.arr[DatenDownload.DOWNLOAD_PROGRAMM_AUFRUF_NR];
    }

    /**
     * Neue Klasse instanzieren
     */
    public RuntimeExec(String p) {
        prog = p;
    }

    //===================================
    // Public
    //===================================
    /**
     * Download starten
     */
    public Process exec() {
        try {
            process = Runtime.getRuntime().exec(prog);
            clearIn = new Thread(new ClearInOut(INPUT, process));
            clearOut = new Thread(new ClearInOut(ERROR, process));
            clearIn.start();
            clearOut.start();
        } catch (Exception ex) {
            //bescheid geben
            if (process == null) {
            }
            Log.fehlerMeldung(450028932,Log.FEHLER_ART_PROG, "RuntimeExec.exec", ex, "Fehler beim Starten");
        }
        return process;
    }

    //===================================
    // Private
    //===================================
    private class ClearInOut implements Runnable {

        private int art;
        private BufferedReader buff;
        private InputStream in;
        private Process process;
        int percent = 0;

        public ClearInOut(int a, Process p) {
            art = a;
            process = p;
        }

        @Override
        public void run() {
            String titel = "";
            try {
                switch (art) {
                    case INPUT:
                        in = process.getInputStream();
                        titel = "INPUTSTREAM";
                        break;
                    case ERROR:
                        in = process.getErrorStream();
                        //TH
                        synchronized (this) {
                            titel = "ERRORSTREAM [" + (++procnr) + "]";
                        }
                        break;
                }
                buff = new BufferedReader(new InputStreamReader(in));
                String inStr;
                while ((inStr = buff.readLine()) != null) {
                    GetPercentageFromErrorStream(inStr);
                    Log.playerMeldung(titel + ": " + inStr);
                }
            } catch (IOException ex) {
            } finally {
                try {
                    buff.close();
                } catch (IOException ex) {
                }
            }
        }

        private void GetPercentageFromErrorStream(String input) {
            // by: siedlerchr
            // für den flvstreamer und rtmpdump
            Matcher matcher = patternFlvstreamer.matcher(input);
            if (matcher.find()) {
                try {
                    prozent = matcher.group();
                    prozent = prozent.substring(0, prozent.length() - 1);
                    // nur ganze Int speichern, damit nur 100 Schritte
                    double d = Double.parseDouble(prozent);
                    meldenDouble(d);
                } catch (Exception ex) {
                    s.datenDownload.statusMelden(DatenDownload.PROGRESS_GESTARTET);
                    Log.fehlerMeldung(912036780, Log.FEHLER_ART_PROG,"RuntimeExec.GetPercentageFromErrorStream-1", input);
                }
            } else {
                // für ffmpeg
                // ffmpeg muss dazu mit dem Parameter -i gestartet werden:
                // -i %f -acodec copy -vcodec copy -y **
                try {
                    matcher = patternFfmpeg.matcher(input);
                    if (matcher.find()) {
                        // Find duration
                        String dauer = matcher.group();
                        String[] hms = dauer.split(":");
                        totalSecs = Integer.parseInt(hms[0]) * 3600
                                + Integer.parseInt(hms[1]) * 60
                                + Double.parseDouble(hms[2]);
                    }
                    matcher = patternZeit.matcher(input);
                    if (totalSecs > 0 && matcher.find()) {
                        zeit = matcher.group();
                        double d = Double.parseDouble(zeit) / totalSecs * 100;
                        meldenDouble(d);
                        //Log.systemMeldung("Filmlänge: " + (int) d + " von " + totalSecs + " s");
                    }
                } catch (Exception ex) {
                    s.datenDownload.statusMelden(DatenDownload.PROGRESS_GESTARTET);
                    Log.fehlerMeldung(912036780, Log.FEHLER_ART_PROG,"RuntimeExec.GetPercentageFromErrorStream-2", input);
                }
            }
        }

        private void meldenDouble(double d) {
            // nur ganze Int speichern, und 1000 Schritte
            d *= 10;
            int pNeu = (int) d;
            if (pNeu != percent) {
                percent = pNeu;
                s.datenDownload.statusMelden(percent);
            }
        }
    }
}
