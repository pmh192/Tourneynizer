package com.tourneynizer.tourneynizer.services;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.util.CookieRequestFactory;
import com.tourneynizer.tourneynizer.util.JSONConverter;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ryanl on 2/28/2018.
 */

public class TeamRequestService {

    public interface OnTeamRequestsLoadedListener {
        public void onTeamRequestsLoaded(TeamRequest[] teamRequests);
    }

    public interface OnRequestCompletedListener {
        public void onRequestCompleted(VolleyError error);
    }

    public void sendRequestToTeam(Team t, final OnRequestCompletedListener listener) {
        String url = HTTPService.DOMAIN + "team/" + t.getID() + "/request";
        CookieRequestFactory factory = new CookieRequestFactory();
        StringRequest request = factory.makeStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Success", "Request Sent");
                listener.onRequestCompleted(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onRequestCompleted(error);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void sendRequestToUser(Team t, User u, final OnRequestCompletedListener listener) {
        String url = HTTPService.DOMAIN + "user/" + u.getId() + "/request/team/" + t.getID();
        CookieRequestFactory factory = new CookieRequestFactory();
        StringRequest request = factory.makeStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Success", "Request Sent");
                listener.onRequestCompleted(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onRequestCompleted(error);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void acceptRequest(final TeamRequest teamRequest, final OnRequestCompletedListener listener) {
        UserService userService = new UserService();
        userService.getSelf(new UserService.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {
                if (user.getId() == teamRequest.getUserID()) {
                    acceptRequestForUser(teamRequest, listener);
                } else {
                    acceptRequestForTeam(teamRequest, listener);
                }
            }
        });
    }

    public void acceptRequestForTeam(TeamRequest tRequest, final OnRequestCompletedListener listener) {
        String url = HTTPService.DOMAIN + "team/" + tRequest.getTeamID() + "/requests/" + tRequest.getID() + "/accept";
        CookieRequestFactory factory = new CookieRequestFactory();
        StringRequest request = factory.makeStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Success", "Request Accepted");
                listener.onRequestCompleted(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onRequestCompleted(error);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void acceptRequestForUser(TeamRequest tRequest, final OnRequestCompletedListener listener) {
        String url = HTTPService.DOMAIN + "user/requests/" + tRequest.getID() + "/accept";
        CookieRequestFactory factory = new CookieRequestFactory();
        StringRequest request = factory.makeStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Success", "Request Accepted");
                listener.onRequestCompleted(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onRequestCompleted(error);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void declineRequest(TeamRequest tRequest, final OnRequestCompletedListener listener) {
        String url = HTTPService.DOMAIN + "requests/" + tRequest.getID();
        CookieRequestFactory factory = new CookieRequestFactory();
        StringRequest request = factory.makeStringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Success", "Request declined");
                listener.onRequestCompleted(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onRequestCompleted(error);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getRequestsForSelf(final OnTeamRequestsLoadedListener listener) {
        String url = HTTPService.DOMAIN + "user/requests/pending";
        CookieRequestFactory factory = new CookieRequestFactory();
        Request request = factory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Team Requests", response.toString());
                TeamRequest[] teamRequests = new TeamRequest[response.length()];
                try {
                    for (int i = 0; i < response.length(); i++) {
                        teamRequests[i] = JSONConverter.getInstance().convertJSONToTeamRequest(response.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    teamRequests = null;
                }
                listener.onTeamRequestsLoaded(teamRequests);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onTeamRequestsLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getRequestsForTeam(Team t, final OnTeamRequestsLoadedListener listener) {
        String url = HTTPService.DOMAIN + "team/" + t.getID() + "/requests/pending";
        CookieRequestFactory factory = new CookieRequestFactory();
        Request request = factory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                TeamRequest[] teamRequests = new TeamRequest[response.length()];
                try {
                    for (int i = 0; i < response.length(); i++) {
                        teamRequests[i] = JSONConverter.getInstance().convertJSONToTeamRequest(response.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    teamRequests = null;
                }
                listener.onTeamRequestsLoaded(teamRequests);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onTeamRequestsLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getRequestsByTeam(Team t, final OnTeamRequestsLoadedListener listener) {
        String url = HTTPService.DOMAIN + "team/" + t.getID() + "/requests/sent";
        CookieRequestFactory factory = new CookieRequestFactory();
        Request request = factory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                TeamRequest[] teamRequests = new TeamRequest[response.length()];
                try {
                    for (int i = 0; i < response.length(); i++) {
                        teamRequests[i] = JSONConverter.getInstance().convertJSONToTeamRequest(response.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    teamRequests = null;
                }
                listener.onTeamRequestsLoaded(teamRequests);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onTeamRequestsLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getRequestsBySelf(final OnTeamRequestsLoadedListener listener) {
        String url = HTTPService.DOMAIN + "user/requests/sent";
        CookieRequestFactory factory = new CookieRequestFactory();
        Request request = factory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                TeamRequest[] teamRequests = new TeamRequest[response.length()];
                try {
                    for (int i = 0; i < response.length(); i++) {
                        teamRequests[i] = JSONConverter.getInstance().convertJSONToTeamRequest(response.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    teamRequests = null;
                }
                listener.onTeamRequestsLoaded(teamRequests);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onTeamRequestsLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }
}
