/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.admin.myapplication.backend;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.*;

public class MyServlet extends HttpServlet {

    static Logger Log = Logger.getLogger("com.example.admin.myapplication.backend.MyServlet");

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Log.info("Got cron message, constructing email.");

//        resp.setContentType("text/plain");
//        resp.getWriter().println("Please use the form to POST to this url");


        // Create a new Firebase instance and subscrive on child events.
        Firebase firebase = new Firebase("https://memoanywhere-4475c.firebaseio.com/todo");
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Build the email message contents using every field from Firebase
                final StringBuilder newItemMessage = new StringBuilder();
                newItemMessage.append("Good Morning! You have the following todo items:\n");

                for ( DataSnapshot todoItem : dataSnapshot.getChildren() ) {
                    for( DataSnapshot field : todoItem.getChildren() ) {
                        newItemMessage.append( field.getKey() )
                                .append(":")
                                .append(field.getValue().toString())
                                .append("\n");
                    }
                }
                // Now Send the email
                Properties props = new Properties();
                Session session = Session.getDefaultInstance(props, null);
                try {
                    Message msg = new MimeMessage(session);

                    // Make sure you substitue your project-id in the email From field
                    msg.setFrom(new InternetAddress("memoanywhere-4475c@appspot.gserviceaccount.com",
                            "memoanywhere"));
                    msg.addRecipient(Message.RecipientType.TO,
                            new InternetAddress("sketchout@hotmail.com","Recipient") );

                    msg.setSubject("Good Morning!");
                    msg.setText( newItemMessage.toString() );

                    Transport.send(msg);

                } catch (MessagingException e  ) {

                    Log.warning(e.getMessage());
                    e.printStackTrace();

                } catch (UnsupportedEncodingException e) {

                    Log.warning(e.getMessage());
                    e.printStackTrace();

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String name = req.getParameter("name");
        resp.setContentType("text/plain");
        if(name == null) {
            resp.getWriter().println("Please enter a name");
        }
        resp.getWriter().println("Hello " + name);
    }
}
