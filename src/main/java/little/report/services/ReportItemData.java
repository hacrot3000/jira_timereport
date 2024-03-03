package little.report.services;

public class ReportItemData {
    public String projectKey;
    public String projectName;
    public String workLogBody;
    public long itemId;
    public String itemSummary;
    public String author;
    public String startDate;
    public long timeWorked;
    

    public String getProjectKey()
    {
        return projectKey;
    }

    public String getWorkLogBody()
    {
        if (workLogBody == null || workLogBody.isEmpty())
            return "";
        return workLogBody.replaceAll("(\r\n|\n)", "<br />");
    }

    public String getProjectName()
    {
        return projectName;
    }

    public long getItemId()
    {
        return itemId;
    }

    public String getItemSummary()
    {
        if (itemSummary == null || itemSummary.isEmpty())
            return "";
        return itemSummary;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getStartDate()
    {
        if (startDate == null || startDate.isEmpty())
            return "";

        String[] arrOfStr = startDate.split(" ", 2);

        return arrOfStr[0];
    }

    public String getTimeWorked()
    {
        return ReportItemData.timeWorkedReadble(timeWorked);
    }

    public long getTimeWorkedRaw()
    {
        return timeWorked;
    }

    public static String timeWorkedReadble(long timeWorked)
    {
        StringBuffer buffer = new StringBuffer();
        // long hourPerDay = 8;

        //int days = (int)(timeWorked / hourPerDay /3600);
        // int hours = (int)((timeWorked / 3600) % hourPerDay);
        int hours = (int)(timeWorked / 3600);
        int minutes = (int)((timeWorked / 60) % 60);
        // int secs = (int)(timeWorked % 60);

        // if (days > 0) // Display days and hours
        // {
        //     buffer.append(Integer.toString(days));
        //     buffer.append("d ");

        //     if (hours > 0 && hours < 10) {
        //         buffer.append('0');
        //     }

        // }

        if (hours > 0) {
            buffer.append(Integer.toString(hours));
            buffer.append("h ");

            if (minutes > 0 && minutes < 10) {
                buffer.append('0');
            }

        }

        if (minutes > 0)
        {
            buffer.append(Integer.toString(minutes));
            buffer.append('m');
        }
        // if (secs < 10) {
        //     buffer.append('0');
        // }
        //buffer.append(Integer.toString(secs));
        return buffer.toString();        
    }
}
