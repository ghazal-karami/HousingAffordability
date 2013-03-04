package au.org.housing.model;

/**
 * Contains the fields for displaying the LGAs to the user.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

public class LGA {
	
	private String lgaCode;
	private String lgaName;
	
	public LGA() {
        
    }
	
	public LGA(String lgaCode, String lgaName) {
        this.lgaCode = lgaCode;
        this.lgaName = lgaName;
    }
	
	/*public Map<String,? extends Object> getLGAsMap(){
		Map<String,List<LGA>> lgaMap = new HashMap<String,List<LGA>>();	
		List<LGA> lGAs = new ArrayList<LGA>();		 
		lgaMap = new HashMap<String,List<LGA>>();		
		lGAs.add(new LGA("333","HUME"));
		lGAs.add(new LGA("349","MOONEE VALLEY"));
		lGAs.add(new LGA("375","WYNDHAM"));
		lGAs.add(new LGA("331","HOBSONS BAY"));
		lGAs.add(new LGA("341","MARIBYRNONG"));
		lGAs.add(new LGA("303","BANYULE"));
		lGAs.add(new LGA("376","YARRA"));
		lGAs.add(new LGA("356","NILLUMBIK"));
		lGAs.add(new LGA("351","MORELAND"));
		lGAs.add(new LGA("308","BRIMBANK"));
		lGAs.add(new LGA("316","DAREBIN"));
		lGAs.add(new LGA("344","MELTON"));
		lGAs.add(new LGA("373","WHITTLESEA"));
		lGAs.add(new LGA("343","MELBOURNE"));
		lgaMap.put("lgas", lGAs);
		return lgaMap;
	}*/
	
	public String getLgaCode() {
		return lgaCode;
	}
	public void setLgaCode(String lgaCode) {
		this.lgaCode = lgaCode;
	}
	public String getLgaName() {
		return lgaName;
	}
	public void setLgaName(String lgaName) {
		this.lgaName = lgaName;
	}
	
	
	

}
