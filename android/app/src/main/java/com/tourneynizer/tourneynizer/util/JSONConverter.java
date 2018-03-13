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

    private Object nullableRetrieverHelper(JSONObject j, String name) {
        if (j.isNull(name)) {
            return null;
        }
        try {
            return j.get(name);
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
            Long refereeID = (Long) nullableRetrieverHelper(mJSON, "getRefId");
            JSONObject children = mJSON.getJSONObject("matchChildren");
            JSONObject teams = children.getJSONObject("teams");
            JSONObject matches = children.getJSONObject("matches");
            Long team1ID = (Long) nullableRetrieverHelper(teams, "id");
            Long team2ID = (Long) nullableRetrieverHelper(teams, "id");
            Long child1ID = (Long) nullableRetrieverHelper(teams, "id");
            Long child2ID = (Long) nullableRetrieverHelper(teams, "id");
            Long score1 = (Long) nullableRetrieverHelper(mJSON, "score1");
            Long score2 = (Long) nullableRetrieverHelper(mJSON, "score2");
            Long time1 = (Long) nullableRetrieverHelper(mJSON, "timeStart");
            Time startTime = null;
            if (time1 != null) {
                startTime = new Time(time1);
            }
            Long time2 = (Long) nullableRetrieverHelper(mJSON, "timeEnd");
            Time endTime = null;
            if (time2 != null) {
                endTime = new Time(time2);
            }
            m = new Match(mJSON.getLong("id"), mJSON.getLong("tournamentId"), mJSON.getInt("matchOrder"), refereeID,  team1ID, team2ID, score1, score1, child1ID, child2ID, mJSON.getString("scoreType"), startTime, endTime);
        } catch (JSONException e) {
            m = null;
        }
        return m;
    }

    public TeamRequest convertJSONToTeamRequest(JSONObject tRequestJSON) {
        TeamRequest tRequest;
        try {
            Boolean accepted = (Boolean) nullableRetrieverHelper(tRequestJSON, "accepted");
            tRequest = new TeamRequest(tRequestJSON.getLong("id"), tRequestJSON.getLong("teamId"), tRequestJSON.getLong("userId"), tRequestJSON.getLong("requesterId"), accepted, new Time(tRequestJSON.getLong("timeRequested")));
        } catch (JSONException e) {
            tRequest = null;
        }
        return tRequest;
    }
}
