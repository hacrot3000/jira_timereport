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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException
    {
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");                  
        String userId = req.getParameter("user");                  
          
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
        List<String> issues = new ArrayList<String>();

        try{
            SqlHelper sqlHelper = SqlHelper.getInstance();
            ResultSet rs = null;
            SqlProcessorIct sqlProcessor = sqlHelper.getSqlProcessor();
            Statement stmt = sqlProcessor.createStatement();
            rs = stmt.executeQuery("SELECT * FROM jiraissue ORDER BY `ID` DESC LIMIT 2");

            while(rs.next()) {
                issues.add(rs.getString("ID"));
            }

            rs.close();
            sqlProcessor.close();

        } catch (SQLException e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }

            throw new RuntimeException(e);
        } 

		context.put("users", users);
		context.put("startDate", startDate);
		context.put("endDate", endDate);
		context.put("userId", userId);
        
        response.setContentType("text/html;charset=utf-8");
        templateRenderer.render("timereport.phtml", context, response.getWriter());
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        this.doGet(req, res);
     }
  

}