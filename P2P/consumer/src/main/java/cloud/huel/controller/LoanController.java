package cloud.huel.controller;

import cloud.huel.constant.DbConstants;
import cloud.huel.constant.KeyConstants;
import cloud.huel.domain.load.BidInfo;
import cloud.huel.domain.load.LoanInfo;
import cloud.huel.domain.user.User;
import cloud.huel.exception.TradeFailException;
import cloud.huel.service.BidInfoService;
import cloud.huel.service.LoanInfoService;
import cloud.huel.vo.ResponseMessage;
import cloud.huel.vo.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 张晓华
 * @date 2022-7-15
 */
@Slf4j
@Controller
@RequestMapping("/loan")
public class LoanController {


	@DubboReference(interfaceClass = LoanInfoService.class, version = "1.0", timeout = 2000, retries = 3)
	private LoanInfoService loanInfoService;

	@DubboReference(version = "1.0", timeout = 2000, retries = 3)
	private BidInfoService bidInfoService;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	@GetMapping("/loanInfo/{productType}")
	public String toLoan(@PathVariable String productType, @RequestParam(value = "page", defaultValue = "1") Integer page,
						 Model model) {
		if ("100".equals(productType)) {
			productType = null;
		}
		List<LoanInfo> loanInfos = loanInfoService.queryLoanInfoByProductTypeForPage(productType, (page - 1) * DbConstants.PAGE_SIZE, DbConstants.PAGE_SIZE);
		Long rows = loanInfoService.queryTotalRows(productType);
		Long pages = null;
		if (rows % DbConstants.PAGE_SIZE == 0) {
			pages = rows / DbConstants.PAGE_SIZE;
		} else {
			pages = rows / DbConstants.PAGE_SIZE;
			pages += 1;
		}
		model.addAttribute("loanInfos", loanInfos);
		model.addAttribute("rows", rows);
		model.addAttribute("currentPage", page);
		model.addAttribute("pages", pages);
		if (productType == null) {
			model.addAttribute("type", "100");
		} else {
			model.addAttribute("type", productType);
		}
		List<Tuple> tuples = bidInfoService.queryRank();
		tuples.forEach(s -> {
			String value = (String) s.getValue();
			value = value.substring(0, 3) + "*****" + value.substring(8, value.length());
			s.setValue(value);
		});
		log.info("获取到的排名信息: " + tuples);
		model.addAttribute("top", tuples);
		return "loan";
	}


	@GetMapping("/loanDetail/{id}")
	public String loanDetail(@PathVariable Integer id, Model model) {
		LoanInfo loanInfo = loanInfoService.queryLoanDetailInfo(id);
		List<BidInfo> investHistory = bidInfoService.queryInvestHistory(id);
		investHistory.forEach(s -> {
			String phone = s.getUser().getPhone();
			phone = phone.substring(0, 3);
			phone += "*****";
			phone += s.getUser().getPhone().substring(8, 11);
			s.getUser().setPhone(phone);
		});
		model.addAttribute("loanInfo", loanInfo);
		model.addAttribute("investHistory", investHistory);
		return "loanInfo";
	}


	@PostMapping("/invest")
	@ResponseBody
	public ResponseMessage invest(Long bidMoney, Integer productId, HttpSession session) throws TradeFailException {
		if (Objects.isNull(bidMoney) || Objects.isNull(productId) || Objects.isNull(session.getAttribute(KeyConstants.USER_SESSION))) {
			return ResponseMessage.error("交易失败");
		}
		User user = (User) session.getAttribute(KeyConstants.USER_SESSION);
		if (Objects.isNull(user.getIdCard())) {
			return ResponseMessage.error("未实名用户不允许交易");
		}
		user = bidInfoService.invest(productId, bidMoney, user);
		if (user == null) {
			return ResponseMessage.error("交易失败,请检查账户信息");
		}
		session.setAttribute(KeyConstants.USER_SESSION, user);
		return ResponseMessage.success();
	}


}
