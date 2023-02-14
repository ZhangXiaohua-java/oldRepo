package cloud.huel.controller;

import cloud.huel.domain.load.LoanInfo;
import cloud.huel.service.BidInfoService;
import cloud.huel.service.LoanInfoService;
import cloud.huel.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author 张晓华
 * @date 2022-7-15
 */
@Slf4j
@Controller
public class IndexController {


	@DubboReference(interfaceClass = UserService.class, version = "1.0", timeout = 3000, retries = 3)
	private UserService userService;

	@DubboReference(interfaceClass = LoanInfoService.class, version = "1.0", timeout = 3000, retries = 3)
	private LoanInfoService loanInfoService;

	@DubboReference(version = "1.0", timeout = 2000, retries = 3)
	private BidInfoService bidInfoService;

	@GetMapping("/")
	public String toIndex(ModelMap modelMap) throws InterruptedException {
		Long count = userService.queryUserCount();
		log.info("查询到用户的总数量为: " + count);
		Double rate = loanInfoService.queryAverageRateYearly();
		log.info("查询到的年化收益率为: " + rate);
		Double totalBidMoney = bidInfoService.queryHistoryTotalBidMoney();
		log.info("查询到的总投资金额: " + totalBidMoney);
		List<LoanInfo> newLoan = loanInfoService.queryLoanInfoByProductTypeForPage("0", 0, 1);
		List<LoanInfo> betterLoans = loanInfoService.queryLoanInfoByProductTypeForPage("1", 0, 4);
		List<LoanInfo> otherLoans = loanInfoService.queryLoanInfoByProductTypeForPage("2", 0, 8);
		modelMap.addAttribute("count", count);
		modelMap.addAttribute("rate", rate);
		modelMap.addAttribute("newLoan", newLoan);
		modelMap.addAttribute("betterLoans", betterLoans);
		modelMap.addAttribute("otherLoans", otherLoans);
		modelMap.addAttribute("totalBidMoney", totalBidMoney);
		log.info("modelMap" + modelMap.toString());
		return "index";
	}


	@GetMapping("/money")
	@ResponseBody
	public Object totalMoney() throws InterruptedException {
		Double totalBidMoney = bidInfoService.queryHistoryTotalBidMoney();
		log.info("总投资金额: " + totalBidMoney);
		return totalBidMoney;
	}


}
