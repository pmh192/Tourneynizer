package com.tourneynizer.tourneynizer.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.util.Pair;

import com.tourneynizer.tourneynizer.model.Match;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentDef;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.util.List;
import java.util.Locale;

/**
 * Created by ryanl on 2/17/2018.
 */

public class JSONConverter {

    private static JSONConverter jsonConverter;

    private Context context;

    private JSONConverter(Context c) {
        context = c.getApplicationContext();
    }

    public static void init(Context c) {
        jsonConverter = new JSONConverter(c);
    }

    public static JSONConverter getInstance() throws NullPointerException {
        if (jsonConverter == null) {
            throw new NullPointerException();
        }
        return jsonConverter;
    }

    private Long nullableLongRetriever(JSONObject j, String name) {
        if (j.isNull(name)) {
            return null;
        }
        try {
            return j.getLong(name);
        } catch (JSONException e) {
            return null;
        }
    }

    public Tournament convertJSONToTournament(JSONObject tJSON) {
        Tournament t;
        try {
            Geocoder coder = new Geocoder(context);
            Address address;
            try {
                // May throw an IOException
                List<Address> addresses = coder.getFromLocation(tJSON.getDouble("lat"), tJSON.getDouble("lng"), 5);
                if (addresses == null || addresses.size() == 0) {
                    address = new Address(Locale.getDefault());
                    address.setLatitude(tJSON.getDouble("lat"));
                    address.setLongitude(tJSON.getDouble("lng"));
                } else {
                    address = addresses.get(0);
                }
            } catch (IOException ex) {
                address = new Address(Locale.getDefault());
                address.setLatitude(10);
                address.setLongitude(100);
            }
            t = new Tournament(tJSON.getLong("id"), tJSON.getString("name"), address, new Time(tJSON.getLong("startTime")), tJSON.getInt("maxTeams"), 0/*ioooootJSON.getInt("currentTeams")*/, new Time(tJSON.getLong("timeCreated")), TournamentType.valueOf(tJSON.getString("type")), tJSON.getLong("creatorId"), tJSON.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
            t = null;
        }
        return t;
    }

    public JSONObject convertTournamentDefToJSON(TournamentDef tDef) {
        JSONObject tJSON = new JSONObject();
        try {
            tJSON.put("name", tDef.getName());
            tJSON.put("description", tDef.getDescription());
            tJSON.put("logo", tDef.getLogo());
            tJSON.put("type", tDef.getTournamentType().name());
            tJSON.put("lat", tDef.getAddress().getLatLng().latitude);
            tJSON.put("lng", tDef.getAddress().getLatLng().longitude);
            tJSON.put("startTime", tDef.getStartTime().getTime());
            tJSON.put("teamSize", tDef.getTeamSize());
            tJSON.put("maxTeams", tDef.getMaxTeams());
        } catch (JSONException e) {
            tJSON = null;
        }
        return tJSON;
    }

    public User convertJSONToUser(JSONObject uJSON) {
        User u;
        try {
            u = new User(uJSON.getLong("id"), uJSON.getString("email"), uJSON.getString("name"), new Time(uJSON.getLong("timeCreated")), 0 /*uJSON.getInt("wins")*/, 0 /*uJSON.getInt("losses")*/, 0 /*uJSON.get("tournamentsParticipated")*/);
        } catch (JSONException e) {
            u = null;
        }
        return u;
    }

    public Team convertJSONToTeam(JSONObject tJSON) {
        Team t;
        try {
            t = new Team(tJSON.getLong("id"), tJSON.getString("name"), new Time(tJSON.getLong("timeCreated")), tJSON.getLong("creatorId"), tJSON.getLong("tournamentId"), tJSON.getBoolean("checkedIn"), true, null);
        } catch (JSONException e) {
            t = null;
        }
        return t;
    }

    public Match convertJSONToMatch(JSONObject mJSON) {
        Match m;
        try {
            Long refereeID = nullableLongRetriever(mJSON, "refId");
            JSONObject children = mJSON.getJSONObject("matchChildren");
            JSONArray teams = children.getJSONArray("teams");
            JSONArray matches = children.getJSONArray("matches");
            Long team1ID;
            if (teams.isNull(0)) {
                team1ID = null;
            } else {
                team1ID = nullableLongRetriever(teams.getJSONObject(0), "id");
            }
            Long team2ID;
            if (teams.isNull(1)) {
                team2ID = null;
            } else {
                team2ID = nullableLongRetriever(teams.getJSONObject(1), "id");
            }
            Long child1ID;
            if (matches.isNull(0)) {
                child1ID = null;
            } else {
                child1ID = nullableLongRetriever(matches.getJSONObject(0), "id");
            }
            Long child2ID;
            if (matches.isNull(1)) {
                child2ID = null;
            } else {
                child2ID = nullableLongRetriever(matches.getJSONObject(1), "id");
            }
            Long score1 = nullableLongRetriever(mJSON, "score1");
            Long score2 = nullableLongRetriever(mJSON, "score2");
            Long time1 = nullableLongRetriever(mJSON, "timeStart");
            Time startTime = null;
            if (time1 != null) {
                startTime = new Time(time1);
            }
            Long time2 = nullableLongRetriever(mJSON, "timeEnd");
            Time endTime = null;
            if (time2 != null) {
                endTime = new Time(time2);
            }
            m = new Match(mJSON.getLong("id"), mJSON.getLong("tournamentId"), mJSON.getInt("matchOrder"), refereeID,  team1ID, team2ID, score1, score2, child1ID, child2ID, mJSON.getString("scoreType"), startTime, endTime, mJSON.getString("matchStatus"));
        } catch (JSONException e) {
            m = null;
        }
        return m;
    }

    public TeamRequest convertJSONToTeamRequest(JSONObject tRequestJSON) {
        TeamRequest tRequest;
        try {
            Boolean accepted;
            if (tRequestJSON.isNull("accepted")) {
                accepted = null;
            } else {
                accepted = tRequestJSON.getBoolean("accepted");
            }
            tRequest = new TeamRequest(tRequestJSON.getLong("id"), tRequestJSON.getLong("teamId"), tRequestJSON.getLong("userId"), tRequestJSON.getLong("requesterId"), accepted, new Time(tRequestJSON.getLong("timeRequested")));
        } catch (JSONException e) {
            tRequest = null;
        }
        return tRequest;
    }
}
