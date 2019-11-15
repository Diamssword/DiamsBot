package diamssword.bot.alexa;

public class YTRequestJson {
	 public String kind;
	 public String regionCode;
	 public PageInfo pageInfo;
	public YTVideoJson[] items;
	
	public YTVideoJson[] getasVideo()
	{
		return (YTVideoJson[]) items;
	}
private static class PageInfo
{
	public int totalResults;
	public int resultsPerPage;
}
}
