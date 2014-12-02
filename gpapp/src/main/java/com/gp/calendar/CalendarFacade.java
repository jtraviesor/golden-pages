package com.gp.calendar;

import java.sql.SQLException;
import java.util.Date;

import com.gp.business.BusinessDAO;
import com.gp.users.UserDAO;
import com.gp.users.UserDTO;

public class CalendarFacade {

  public boolean addEntry(String hour, String date, String businessId, String inviterId,
      CalendarDTO.Type type) {

    UserDAO userDAO = new UserDAO("jdbc/gpappdb");
    CalendarDAO calendarDAO = new CalendarDAO("jdbc/gpappdb");

    Date d = ParseDate.parsingDate(date, hour);
    if (d == null)
      return false;
    CalendarDTO calendar = new CalendarDTO();
    calendar.setDate(new java.sql.Timestamp(d.getTime()));


    switch (type) {
      case B2C:
        // businessDAO.
        break;
      case C2B:
        break;
      default:
        return false;
    }

    boolean valid = true;

    try {


      Integer managerId = userDAO.findManagerId(businessId);
      if (managerId == -1)
        return false;
      UserDTO invitee = userDAO.find(managerId.toString());
      if (invitee == null)
        return false;
      calendar.setInviteeId(invitee.getUid());

      if (inviterId == null)
        return false;
      calendar.setInviterId(inviterId);
      calendar.setStatus(CalendarDTO.Status.PENDING);
      calendar.setType(CalendarDTO.Type.C2B);
      calendarDAO.create(calendar);
      
      
      
      
      

    } catch (Exception e) {
      e.printStackTrace();
      valid = false;
    }


    return valid;
  }

  public CalendarBean find(UserDTO user) {
    CalendarBean bean = new CalendarBean();
    CalendarDAO dao = new CalendarDAO("jdbc/gpappdb");
    bean.setApptList(dao.find(user));
    return bean;
  }

}
