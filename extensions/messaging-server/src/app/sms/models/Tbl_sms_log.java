// SQL_db: sms_server
// SQL_table: sms_log
// Mono Models
// Monosync Framewrok v9.08.16
// Generated using LazyMono
// This code is computer generated, do not modify
// Author: Jhon Melvin Nieto Perello
// Contact: jhmvinperello@gmail.com
//
// The Framework uses Hibernate as its ORM
// For more information about Hibernate visit hibernate.org
package app.sms.models;

/**
 *
 * @author Jhon Melvin
 */
public class Tbl_sms_log {
public final String id = "id";
public final String gateway_id = "gateway_id";
public final String message_uuid = "message_uuid";
public final String date = "date";
public final String recipient = "recipient";
public final String dispatch_date = "dispatch_date";
public final String message_status = "message_status";
public final String failure_cause = "failure_cause";
public final String message_body = "message_body";
public final String pdu_data = "pdu_data";
public final String entry_date = "entry_date";
public final String active = "active";
}