package com.lottomo.main.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.lottomo.main.model.DailyWin;
import com.lottomo.main.model.DeleteButton;
import com.lottomo.main.model.Entry;
import com.lottomo.main.model.LottoNumber;
import com.lottomo.main.model.Settings;
import com.lottomo.main.interfaces.IMainAgent;
import com.lottomo.main.interfaces.ISubAgent;
import com.lottomo.main.intrfc.EntryNums;
import com.lottomo.main.model.BetInput;
import com.lottomo.main.model.CONSTANTS;
import com.lottomo.main.service.AgentService;
import com.lottomo.main.service.EntryService;
import com.lottomo.main.service.SettingsService;

@Controller
@RequestMapping("/")
public class AppController {
	
	@Autowired
	EntryService entryService;
	@Autowired 
	AgentService agentService;
	@Autowired 
	SettingsService settingsService;
	
	private List<Entry> listBets = new ArrayList<>();
	private LottoNumber lottonum = new LottoNumber();	
	//private List<IMainAgent> mainagentList = new ArrayList<>();
	private List<ISubAgent> subagentList = new ArrayList<>();
	private static Integer voidMaxTotal=0;
	private static ConcurrentHashMap<String,Integer> voidMaxTrackerMap = new ConcurrentHashMap<>();
	
	@GetMapping("/")
	public String viewHomePage(Model model) {
		//List<EntryNums> nums = entryService.getNumbersByName("@Evil Oreo");
		//System.out.println(" getNumbersByName: "+ nums.get(0).getNum1()+ " "+ nums.get(0).getNum2()+" "+ nums.get(0).getNum3() );
		return viewHomePage(model, 1, "mainagent", "asc");
	}
	
	@GetMapping("/home/{pageNum}")
	public String viewHomePage(Model model, 
			@PathVariable(name = "pageNum") int pageNum,
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir) {
		
	//	Page<Entry> page = entryService.listAll(pageNum, sortField, sortDir);
		List<Entry> entryList = entryService.listAllEntries();
	    for(Entry entry: entryList) {
	    	Integer num1 = entry.getNum1();
	    	Integer num2 = entry.getNum2();
	    	Integer num3 = entry.getNum3();
	    	List<Integer> sortNums = Arrays.asList(num1, num2,num3);
	    	Collections.sort(sortNums);
	    	entry.setOrderedNums(String.valueOf(sortNums.get(0))+"-"+String.valueOf(sortNums.get(1))+"-"+String.valueOf(sortNums.get(2)));
	    }
    
		
		Entry entry = new Entry();
		BetInput betinput = new BetInput();
		DeleteButton delbttn = new DeleteButton();
		
		List<Long> idList = new ArrayList<>();
		for(Entry entryObj: entryList ) {
			idList.add(entryObj.getEntryid());
		}
		
		model.addAttribute("delbttn", delbttn);
		model.addAttribute("entry", entry);
		model.addAttribute("betinput", betinput);	
		model.addAttribute("currentPage", pageNum);		
//		model.addAttribute("totalPages", page.getTotalPages() );
//		model.addAttribute("totalItems", page.getTotalElements() );
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
	//	model.addAttribute("viewEntryList", page.getContent() ); entryList
		model.addAttribute("viewEntryList", entryList ); 
		return "index";
	}	
	
	@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Long id) {
		//service.delete(id);
		return "redirect:/";
	}
	
	@GetMapping("/deletealldata")
	public String deleteAllEntries() {
		entryService.deleteAllEntries();
		return "deletealldata";
	}

	@PostMapping("/saveentry")
	public String addEntry(@ModelAttribute("betinput") BetInput betinput) {
		parseAndSaveTxtArea(betinput.getInputbox().trim(), betinput.getCategory());
		return "redirect:/";
	}
	
	private Map<String,String> getAgents(String textinput) {
		String[] getfirstline = textinput.split("\n");
		String[] getagents = getfirstline[0].split("=");
		String[] getagents2 = getagents[1].split("-");
		
		//Agent bettingAgents = new Agent();
		Map<String,String> agentsmap = new ConcurrentHashMap<>();
		
		agentsmap.put("mainagent", getagents2[0]);
		agentsmap.put("subagent", getagents2[1]);
		return agentsmap;
	}
	
	private int parseAndSaveTxtArea (String txtArea, Integer category) {
		String rgx_1 = "(?s)(ID.*?)\\s*(?=ID|$)";
		Pattern pattern_1 = Pattern .compile(rgx_1);
		Matcher matcher_1 = pattern_1.matcher(txtArea.trim());
	
		//SEGREGATE BY ID
		while (matcher_1.find()) {
		    String rgx_2 = "(?s)(@.*?)\\s*(?=@|$)";
			Pattern pattern_2 = Pattern .compile(rgx_2);
			Matcher matcher_2 = pattern_2.matcher(matcher_1.group(1));
			Map<String,String> agentsmap= getAgents(matcher_1.group(1));
			
			//SEGREGATE BY @Player
			while (matcher_2.find()) {
			    String[] playerData =  matcher_2.group(1).split("\n");
			    List<String> playerDataList = Arrays.asList(playerData);
			    for(int i = 1; i<playerDataList.size() ; i++) {
			    	List<String> numsAndBetList = getNums(playerDataList.get(i));
			    	List<Integer> numList = new ArrayList<>();
			    	numList.add(Integer.valueOf( numsAndBetList.get(0)));
			    	numList.add(Integer.valueOf( numsAndBetList.get(1)));
			    	numList.add(Integer.valueOf( numsAndBetList.get(2)));
			    	
			    	Entry entry = new Entry();
					entry.setPlayername(playerDataList.get(0)); //@Playername
					entry.setMainagent(agentsmap.get("mainagent"));
					entry.setSubagent( agentsmap.get("subagent"));
					entry.setDatelastmodified(LocalDateTime.now());
					entry.setNum1( Integer.valueOf( numsAndBetList.get(0)) ); //bet num1
					entry.setNum2( Integer.valueOf( numsAndBetList.get(1)) ); //bet num2
					entry.setNum3( Integer.valueOf( numsAndBetList.get(2)) ); //bet num3
			    	//for FreeBet cases
					if(numsAndBetList.get(3).equalsIgnoreCase("FB")){
						entry.setAmount(10);
						entry.setFreebet(CONSTANTS.FREEBET);
						entry.setReturnbet(0);
					//for ReturnBet cases
					}else if(numsAndBetList.get(3).contains("RB")){
						Integer returnbet = Integer.valueOf( numsAndBetList.get(3).substring(0, numsAndBetList.get(3).length()-2).trim());
						System.out.println("returnbet "+returnbet);
						entry.setAmount(returnbet);
						entry.setFreebet(0);
						entry.setReturnbet(returnbet);
					//for normal cases
					}else {
						entry.setAmount(Integer.valueOf( numsAndBetList.get(3)) ); 
						entry.setFreebet(0);
						entry.setReturnbet(0);
					}
					//categories - 42,45,49,55
					entry.setCategory(category);
					//PROCESS STATUS
					
					//1.check if VOID_LIMIT
					boolean isVoidLimit = checkIfVoidLimit(numList,category);
					//2.check if VOID_DOUBLE
					boolean isVoidDouble= checkIfVoidDouble(numList);
					//3.check if VOID_DUPLICATE 
					boolean isVoidDuplicate = checkIfVoidDuplicate(numList,playerDataList.get(0));//num1,num2,num3 and playername
					//4.check if VOID_MAX
					boolean isVoidMax = checkIfVoidMax(numList, category);
					//status is 'good bet' by default, but..
					entry.setStatus(CONSTANTS.GOOD_BET);
					
					if(isVoidMax) {
						entry.setStatus(CONSTANTS.VOID_MAX);
					}
					if(isVoidLimit) {
						entry.setStatus(CONSTANTS.VOID_LIMIT);
					}
					if(isVoidDouble) {
						entry.setStatus(CONSTANTS.VOID_DOUBLE);
					}
					if(isVoidDuplicate) {
						entry.setStatus(CONSTANTS.VOID_DUPLICATE);
					}
					
			    	entryService.saveEntry(entry);
			    	entry=null;

			    }
		     }
		}
		voidMaxTrackerMap.clear();
		return 0;
	}

	private List<String> getNums(String line){
		String[] nums = line.trim().split("-");
		List<String> numsList = new ArrayList<>();
		String[] thirdnum = nums[2].split("=");
		
		numsList.add(nums[0].trim()); 		//1st num
		numsList.add(nums[1].trim()); 		//2nd num
		numsList.add(thirdnum[0].trim());  //3rd num
		numsList.add(thirdnum[1].trim());  //bet amount
		return numsList;
	}
	
	@GetMapping("/result")
	public String gotToResult(Model model) {
		
		Entry entry = new Entry();
		DailyWin dailywin = new DailyWin();
		model.addAttribute("entry", entry);
		model.addAttribute("dailywin", dailywin);
		model.addAttribute("listBets", listBets);
		model.addAttribute("lottonum",lottonum);
		
		return "result";
	}
	
	@PostMapping("/result")
	public String getLuckyWinner(@ModelAttribute("dailywin") DailyWin dailywin) {
		//System.out.println("find all entries -> "+ entryService.searchWinner(null, null));
		System.out.println("Postmapping /result "+dailywin.toString()); 
		
		List<Integer> winningCombinations = new ArrayList<>();
		winningCombinations.add(dailywin.getNum1());
		winningCombinations.add(dailywin.getNum2());
		winningCombinations.add(dailywin.getNum3());
		winningCombinations.add(dailywin.getNum4());
		winningCombinations.add(dailywin.getNum5());
		winningCombinations.add(dailywin.getNum6());
		
		listBets.clear();
		lottonum.setLottonumbers("");
		
//		lottonum.setLottonumbers(dailywin.getNum1()+" "+dailywin.getNum2()+" "+dailywin.getNum3()+" "+dailywin.getNum4()+" "
//				   +dailywin.getNum5()+" "+dailywin.getNum6());
		
		lottonum.setLottonumbers(numbers1To9 (dailywin.getNum1())+" - "+ numbers1To9(dailywin.getNum2())+" - "+numbers1To9(dailywin.getNum3())+" - "+numbers1To9(dailywin.getNum4())+" - "
				   +numbers1To9(dailywin.getNum5())+" - "+numbers1To9(dailywin.getNum6()));

		
		//1. get all records for current day only. ADD 'today only' filter later
		List<Entry> listDailyEntries = entryService.listAllEntries();
		
		//2. traverse one by one if num1-num3 is in daily winner combination
		for(int i = 0; i < listDailyEntries.size(); i++){
			boolean isMatchNum1 = findMatches( listDailyEntries.get(i).getNum1(), winningCombinations);
			boolean isMatchNum2 = findMatches( listDailyEntries.get(i).getNum2(), winningCombinations);
			boolean isMatchNum3 = findMatches( listDailyEntries.get(i).getNum3(), winningCombinations);
			
			//3. if YES, add to betlists
			if(isMatchNum1 && isMatchNum2 && isMatchNum3) listBets.add(listDailyEntries.get(i));			
			
		}
		
		
		return "redirect:/result";
	}

	public String numbers1To9 (Integer n) {
		return (n < 10) ? ("0" + n) : Integer.toString(n);
	}
	
	public Boolean findMatches(Integer num, List<Integer> listOfWinningNumbers) {
		return listOfWinningNumbers.stream().anyMatch(i -> i == num );
	}
	
	@GetMapping("/clearbetlist")
	public String clearBetList() {
		listBets.clear();
		lottonum.setLottonumbers("");
		return "clearbetlist";
	}
	
	@GetMapping("/agentreport")
	public String gotoAgentReport(Model model) {
		return viewMainAgentPage(model, 1);
	}
	
	
	@GetMapping("/mainagent/{pageNum}")
	public String viewMainAgentPage(Model model, 
			@PathVariable(name = "pageNum") int pageNum) {
		
		Page<IMainAgent> pagelist = agentService.findAllMainAgentByGroup(pageNum);
		System.out.println("page.getContent() "+pagelist );
		System.out.println("totalItems "+pagelist.getTotalElements() );
		
		model.addAttribute("currentPage", pageNum);		
		model.addAttribute("totalPages", pagelist.getTotalPages() );
		model.addAttribute("totalItems", pagelist.getTotalElements());

		model.addAttribute("mainagentList", pagelist.getContent()  );
		return "agentreport";
	}	
	
	
	@GetMapping("/subagentreport")
	public String gotoSubAgentReport(Model model) {
		
		subagentList = agentService.findAllSubAgentByGroup();
		model.addAttribute("subagentList",subagentList);
		
		for(ISubAgent agent: subagentList) {
			System.out.println(agent.getMainagent()+" : "+agent.getAmount() );
		}
		return "subagentreport";
	}
	@GetMapping("/returnbets")
	public String gotoReturnBets() {
	
		return "returnbets";
	}

	@GetMapping("/manageagents")
	public String gotoManageAgents() {
		return "manageagents";
	}
	
	@GetMapping("/wonamount")
	public String gotoWonAmount() {
		return "wonamount";
	}
	
	@GetMapping("/systemsettings")
	public String gotoSystemSettings(Model model) {
		List<Settings> settingsList = settingsService.listAllSettings();
		
		model.addAttribute("settingsList",settingsList);
		
		return "systemsettings";
	}
	
	@RequestMapping("/edit/{entryid}")
	public String edit(@PathVariable Long entryid,Model model) {
        try {
        	Entry entry = entryService.findById(entryid);
        	model.addAttribute("entry",entry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "edit";
	}	
	
	@GetMapping("/delete/{entryid}")
	public String deleteEntryById(@PathVariable Long entryid) {
		System.out.println("deleting: "+entryid);
		try {
			 entryService.deleteEntryById(entryid);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}
	
	@PostMapping("/updateentry")
	public String updateEntry(Entry entry) {
		System.out.println("updating entryid: "+entry.getEntryid());
		try {
			 entryService.saveEntry(entry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/";

	}
	@RequestMapping("/editsettings/{settingsid}")
	public String editSettings(@PathVariable Long settingsid,Model model) {
        try {
        	Settings settings = settingsService.findById(settingsid);
        	model.addAttribute("settings",settings);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "editsettings";
	}	
	@PostMapping("/updatesettings")
	public String updateSettings(Settings settings) {
		try {
			 settingsService.saveSettings(settings);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/";

	}
	@PostMapping("/multipledelete")
	public String multipleDelete(@ModelAttribute("delbttn")DeleteButton delbttn) {
		int arraysize = delbttn.getLongList().size();
		System.out.println("MULTIPLE DELETE..size of array .. "+ arraysize);

		for( int i = 0; i < arraysize; i++) {
			entryService.deleteEntryById( delbttn.getLongList().get(i));
		}
		
		return "redirect:/";
	}

	//1.check if VOID_LIMIT ,i.e., the number exceeded the category
	public boolean checkIfVoidLimit(List<Integer> numList, Integer limit) {
		for(Integer num: numList) {
			if(num > limit) {
				return true;
			}
		}
		return false;
	}
	//2.check if VOID_DOUBLE ,i.e.,one of the numbers in the entry is a repeat
	public boolean checkIfVoidDouble(List<Integer> numList) {
		//returns true if there's a duplicate
		return findDuplicates(numList).size() > 0;
	}
	//3.check if VOID_DUPLICATE ,i.e., the entry has a duplicate from DB
	public boolean checkIfVoidDuplicate(List<Integer> numList,String playername) {
		List<List<Integer>> listOfIntgrLists = entryService.getNumbersByName(playername);
		Collections.sort(numList);
		
		if(listOfIntgrLists.size() > 0) {
			for(int i=0; i<listOfIntgrLists.size(); i++) {
				String dbNums = String.valueOf(listOfIntgrLists.get(i).get(0))+String.valueOf(listOfIntgrLists.get(i).get(1))+String.valueOf(listOfIntgrLists.get(i).get(2) );
				String paramNums = String.valueOf(numList.get(0))+String.valueOf(numList.get(1))+String.valueOf(numList.get(2));
				if(dbNums.equalsIgnoreCase(paramNums)) return true;
			}
		}
		return false;
	}
	//4.check if VOID_MAX ,i.e.,the sum of the betting amount of all players exceed the category MAX value
	public boolean checkIfVoidMax(List<Integer> numsList,Integer category) {
		Integer maxlimit = entryService.getMaxLimit(category);
		//1.fetch all numbers from db
		List<List<Integer>> listOfIntgrLists = entryService.getAllEntryNumbersAsc();
		//2.sort asc
		Collections.sort(numsList);
		
		Integer entrySum = numsList.get(0)+numsList.get(1)+numsList.get(2);
		String strParamNums = String.valueOf(numsList.get(0))+String.valueOf(numsList.get(1))+String.valueOf(numsList.get(2));
		System.out.println("initiating entrySum : "+entrySum);
		if(entrySum>=maxlimit) {
			return true;
		}
		
		Integer storedVoidMaxTotal = voidMaxTrackerMap.get(strParamNums);
	//	System.out.println("initiating storedVoidMaxTotal.. "+storedVoidMaxTotal);
		
		//check if current numList has  duplicates in DB
		if(listOfIntgrLists.size() > 0) {
			for(int i=0; i<listOfIntgrLists.size(); i++) {
				String strDbNums = String.valueOf(listOfIntgrLists.get(i).get(0))+String.valueOf(listOfIntgrLists.get(i).get(1))+String.valueOf(listOfIntgrLists.get(i).get(2) );
				Integer dbEntrySum = listOfIntgrLists.get(i).get(0)+listOfIntgrLists.get(i).get(1)+listOfIntgrLists.get(i).get(2);

				//3.find if current num1-num3 has duplicates in DB
				if(strDbNums.equalsIgnoreCase(strParamNums)){ 
					
					storedVoidMaxTotal = voidMaxTrackerMap.get(strParamNums);
					if(storedVoidMaxTotal==null) storedVoidMaxTotal = 0;
					
					voidMaxTotal = entrySum + dbEntrySum+storedVoidMaxTotal;
					
					if(voidMaxTotal >= maxlimit){
						return true;
					}else {
						voidMaxTrackerMap.put(strParamNums, voidMaxTotal);
					}
//					if(voidMaxTrackerMap.get(strParamNums)==null) {
//						
//					}else {
//						Integer prevMaxTotal = voidMaxTrackerMap.get(strParamNums);
//						voidMaxTotal = voidMaxTotal + prevMaxTotal;
//					}
				}
			}
		} 
		//check if current numList has  duplicates in current batch
		if(storedVoidMaxTotal != null){
			entrySum = entrySum+ storedVoidMaxTotal;
			//System.out.println("entrySum "+entrySum);
		//	System.out.println("storedVoidMaxTotal "+storedVoidMaxTotal);
			if(entrySum >= maxlimit) {
				voidMaxTrackerMap.clear();
				voidMaxTrackerMap.put(strParamNums, entrySum);
				return true;
			}
		}else {
			voidMaxTrackerMap.put(strParamNums, entrySum);
			//return false;
		}
		
		//voidMaxTotal = voidMaxTotal + playerbet;	
	//	System.out.println("totalPlayersBet "+voidMaxTotal);
	///	System.out.println("maxlimit "+maxlimit);
		//return true if bet amount is greater than void max limit
		return false;
	}
	
	 public static <T> Set<T> findDuplicates(List<T> list) {
	        Set<T> items = new HashSet<>();
	        return list.stream()
	                .filter(n -> !items.add(n))
	                .collect(Collectors.toSet());
	 }
	
}
