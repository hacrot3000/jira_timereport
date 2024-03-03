package little.report.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.common.collect.Maps;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.jira.bc.user.search.UserSearchParams;
import com.atlassian.jira.user.ApplicationUser;
import java.util.Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ofbiz.core.entity.ConnectionFactory;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import org.ofbiz.core.entity.GenericEntityException;
import java.sql.SQLException;

import little.report.services.SqlHelper;
import little.report.services.SqlProcessorIct;
import little.report.services.ReportItemData;

import java.time.LocalDate;
import java.time.YearMonth;

public class littlerepor extends HttpServlet{
    private static final Logger log = LoggerFactory.getLogger(littlerepor.class);

	private final UserManager userManager;
	private final LoginUriProvider loginUriProvider;
	private final TemplateRenderer templateRenderer;
	private final PluginSettingsFactory pluginSettingsFactory;

	public littlerepor(UserManager userManager, LoginUriProvider loginUriProvider, TemplateRenderer templateRenderer, PluginSettingsFactory pluginSettingsFactory) {
		this.userManager = userManager;
		this.loginUriProvider = loginUriProvider;
		this.templateRenderer = templateRenderer;
		this.pluginSettingsFactory = pluginSettingsFactory;
	}

    private String formatDate(String strDate)
    {
        String[] arrOfStr = strDate.split("-", 3);
        int check;

        check = Integer.parseInt(arrOfStr[1]);
        if (check < 10)
            arrOfStr[1] = "0" + check;
        else
            arrOfStr[1] = "" + check;

        check = Integer.parseInt(arrOfStr[2]);
        if (check < 10)
            arrOfStr[2] = "0" + check;
        else
            arrOfStr[2] = "" + check;
    
        return Integer.parseInt(arrOfStr[0]) + "-" + arrOfStr[1] + "-" + arrOfStr[2];

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException
    {
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");                  
        String userIds[] = req.getParameterValues("user");
        String kings = req.getParameter("kind");
        int kind;
        
        if (kings == null) 
        {
            kind = 1;
        }
        else
        {
            kind = Integer.parseInt(req.getParameter("kind"));
        }
          
        UserSearchService userSearchService = ComponentAccessor.getComponent(UserSearchService.class);
        UserSearchParams userSearchParamsActive = (new UserSearchParams.Builder()).allowEmptyQuery(true).includeActive(true).includeInactive(false).maxResults(100000).build();
        UserSearchParams userSearchParamsInactive = (new UserSearchParams.Builder()).allowEmptyQuery(true).includeActive(false).includeInactive(true).maxResults(100000).build();
       
        List<ApplicationUser> users = new ArrayList<ApplicationUser>();

        for (ApplicationUser cuser : userSearchService.findUsers("", userSearchParamsActive)) {
            users.add(cuser);
        }

        for (ApplicationUser cuser : userSearchService.findUsers("", userSearchParamsInactive)) {
            users.add(cuser);
        }

        Map<String, Object> context = Maps.newHashMap();
        List<ReportItemData> issues = null;

        if (startDate != null && !startDate.isEmpty() )
        {
            context.put("takingSearch", true);
            issues = new ArrayList<ReportItemData>();

            startDate = this.formatDate(startDate);
            endDate = this.formatDate(endDate);

            try{
                SqlHelper sqlHelper = SqlHelper.getInstance();
                ResultSet rs = null;
                SqlProcessorIct sqlProcessor = sqlHelper.getSqlProcessor();
                Statement stmt = sqlProcessor.createStatement();

                String sql;

                String usersWhere = "";

                if (userIds != null && userIds.length > 0) 
                {

                    for (String userid : userIds) 
                    { 
                        if (usersWhere.isEmpty())
                        {
                            usersWhere = "w.AUTHOR = '" + userid.replaceAll("'", "") + "'";
                        }
                        else
                        {
                            usersWhere += " OR w.AUTHOR = '" + userid.replaceAll("'", "") + "'";
                        }
                            
                    }
                    usersWhere = " AND (" + usersWhere + ") ";
                }
                
                if (kind == 1)
                {
                    sql = "" +
                        "SELECT p.pkey, p.pname, i.ISSUENUM ID, i.SUMMARY, w.AUTHOR, w.STARTDATE, w.worklogbody, w.timeworked " +
                        "FROM worklog w " +
                        "LEFT JOIN jiraissue i on w.issueid = i.id " +
                        "LEFT JOIN project p on p.ID = i.PROJECT " +
                        "WHERE w.STARTDATE BETWEEN '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59' " +
                        usersWhere +
                        "ORDER BY w.STARTDATE, p.pkey";
                }
                else
                {
                    sql = "" +
                        "SELECT w.AUTHOR, p.pkey, p.pname, sum(w.timeworked) as timeworked " +
                        "FROM worklog w " +
                        "LEFT JOIN jiraissue i on w.issueid = i.id " +
                        "LEFT JOIN project p on p.ID = i.PROJECT " +
                        "WHERE w.STARTDATE BETWEEN '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59' " +
                        usersWhere +
                        "GROUP BY w.AUTHOR, p.pkey, p.pname " +
                        "ORDER BY p.pkey ";
                }

                rs = stmt.executeQuery(sql);

                long totalHours = 0;

                while(rs.next()) {
                    ReportItemData ni = new ReportItemData();

                    if (kind == 1)
                    {
                        ni.itemId = rs.getLong("ID");
                        ni.itemSummary = rs.getString("SUMMARY");
                        ni.startDate = rs.getString("STARTDATE");
                        ni.workLogBody = rs.getString("worklogbody");
                    }
                    ni.projectKey = rs.getString("pkey");
                    ni.projectName = rs.getString("pname");
                    ni.author = rs.getString("AUTHOR");
                    ni.timeWorked = rs.getLong("timeworked");

                    totalHours += ni.timeWorked;

                    issues.add(ni);
                }
                String totalHourStr = ReportItemData.timeWorkedReadble(totalHours);
                context.put("totalHourStr", totalHourStr);

                rs.close();
                sqlProcessor.close();

            } catch (SQLException e) {
                if (log.isDebugEnabled()) {
                    log.error(e.getMessage(), e);
                }

                throw new RuntimeException(e);
            } 
        }
        else
        {
            context.put("takingSearch", false);
        }


        if (startDate == null || startDate.isEmpty())
        {
            LocalDate now = LocalDate.now();
            LocalDate earlier = now.minusMonths(1);
            
            int month = earlier.getMonth().getValue();
            int year = earlier.getYear(); 
            YearMonth yearMonthObject = YearMonth.of(year, month);
            int daysInMonth = yearMonthObject.lengthOfMonth(); 
            String months = month + "";

            if (month < 10)
            {
                months = "0" + months;
            }

            startDate = year + "-" + months + "-01";
            endDate = year + "-" + months + "-" + daysInMonth;
        }
		context.put("users", users);
		context.put("startDate", startDate);
		context.put("endDate", endDate);
		context.put("userIds", userIds);
		context.put("kind", kind);
		context.put("issues", issues);

        String baseUrl = ComponentAccessor.getWebResourceUrlProvider().getBaseUrl();
        context.put("baseUrl", baseUrl);
        
        response.setContentType("text/html;charset=utf-8");
        templateRenderer.render("timereport.phtml", context, response.getWriter());
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        this.doGet(req, res);
     }
  

}