package com.sjh.action;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oracle.net.aso.a;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient.BlockChainInfo;

import com.sjh.entity.Account;
import com.sjh.entity.Blockinfo;
import com.sjh.entity.blockchain;
import com.sjh.service.imp.BcServiceImp;
import com.sjh.util.DateTime;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/bcc")
public class BcController2 {

	@Resource
	BcServiceImp bcserviceImp;

	@RequestMapping("/getblockinfo")
	public String getblockinfo(Model model) throws MalformedURLException {
		BitcoindRpcClient rpcClient = new BitcoinJSONRPCClient(
				"http://RPCuser:RPCpasswd@localhost:8332");
		BlockChainInfo info = rpcClient.getBlockChainInfo();
		Blockinfo blockinfo = new Blockinfo();
		blockinfo.setBestBlockHash(info.bestBlockHash());
		blockinfo.setHeight(info.blocks());
		blockinfo.setChain(info.chain());
		blockinfo.setChainwork(info.chainWork());
		blockinfo.setDifficulty(info.difficulty());
		blockinfo.setHashcode(info.hashCode());
		blockinfo.setToString(info.toString());
		model.addAttribute("blockinfo", blockinfo);
		return "block/getinfo";
	}

	@RequestMapping("/listaccounts")
	public String listaccounts(Model model) throws MalformedURLException {
		BitcoindRpcClient rpcClient = new BitcoinJSONRPCClient(
				"http://RPCuser:RPCpasswd@localhost:8332");
		List<Account> list = new ArrayList<>();
		Map<String, Number> accountsMap = rpcClient.listAccounts();
		for (Map.Entry<String, Number> account : accountsMap.entrySet()) {
			System.out.println("Key = " + account.getKey() + ", Value = "
					+ account.getValue());
			Account a = new Account();
			a.setName(account.getKey());
			a.setNumber(account.getValue());
			a.setAddress(rpcClient.getAddressesByAccount(account.getKey()).get(
					0));

			list.add(a);
		}

		model.addAttribute("list", list);
		return "block/listaccounts";
	}
	@RequestMapping("/addaccount")
	public String addaccount(Model model,String accountname) throws MalformedURLException {
		BitcoindRpcClient rpcClient = new BitcoinJSONRPCClient(
				"http://RPCuser:RPCpasswd@localhost:8332");
		rpcClient.getNewAddress(accountname);
		List<Account> list = new ArrayList<>();
		Map<String, Number> accountsMap = rpcClient.listAccounts();
		for (Map.Entry<String, Number> account : accountsMap.entrySet()) {
			System.out.println("Key = " + account.getKey() + ", Value = "
					+ account.getValue());
			Account a = new Account();
			a.setName(account.getKey());
			a.setNumber(account.getValue());
			a.setAddress(rpcClient.getAddressesByAccount(account.getKey()).get(
					0));

			list.add(a);
		}

		model.addAttribute("list", list);
		return "block/listaccounts";
	}
	
	@RequestMapping("/gosendtoaddress")
	public String gosendtoaddress(Model model) throws MalformedURLException {

		return "block/sendmoney";
	}
	@RequestMapping("/sendtoaddress")
	public String sendtoaddress(Model model,String address,String coin) throws MalformedURLException {
		BitcoindRpcClient rpcClient = new BitcoinJSONRPCClient(
				"http://RPCuser:RPCpasswd@localhost:8332");
		String txhash=rpcClient.sendToAddress(address, Double.parseDouble(coin));
		model.addAttribute("txhash", txhash);
		return "block/txhash";
	}
	@RequestMapping("/json")
	public String bclist(Model model) {
		List<blockchain> lists = new ArrayList<blockchain>();
		lists = bcserviceImp.getAllbcs();
		// JSONArray array=JSONArray.fromObject(list);
		JSONArray listArray = JSONArray.fromObject(lists);
		model.addAttribute("list", listArray);
		// String reString ={code: 0,msg: "",count: 1000,data: listArray}
		return "mid/Pform2";
	}

	@RequestMapping("/add")
	@ResponseBody
	public blockchain add(String productname, String modelNumber,
			String specifications, String inspector, String productionTime) {
		blockchain blockchain = new blockchain();
		blockchain.setBlckey(null);
		blockchain.setCounter(null);
		blockchain.setEnclosure(null);
		blockchain.setThisnumber(null);
		blockchain.setInspector(inspector);
		blockchain.setModelnumber(modelNumber);
		blockchain.setProductiontime(productionTime);
		blockchain.setProductname(productname);
		blockchain.setSpecifications(specifications);

		bcserviceImp.insert(blockchain);

		return blockchain;
	}

	@RequestMapping("/addedit")
	@ResponseBody
	public blockchain addedit(String productname, String modelNumber,
			String specifications, String inspector, String productionTime) {
		blockchain blockchain = new blockchain();
		blockchain.setBlckey(null);
		blockchain.setCounter(null);
		blockchain.setEnclosure(null);
		blockchain.setThisnumber(null);
		blockchain.setInspector(inspector);
		blockchain.setModelnumber(modelNumber);
		blockchain.setProductiontime(productionTime);
		blockchain.setProductname(productname);
		blockchain.setSpecifications(specifications);

		// bcserviceImp.insert(blockchain);
		bcserviceImp.updateByPrimaryKey(blockchain);
		return blockchain;

	}

	@RequestMapping("/deleteaaa")
	public String deleteaaa(Model model, int id) {
		return "mid/Pform2";
	}

	@RequestMapping("/delcc")
	public String del(Model model, int id) {
		bcserviceImp.deleteByPrimaryKey(id);
		List<blockchain> lists = bcserviceImp.getAllbcs();
		JSONArray listArray = JSONArray.fromObject(lists);
		model.addAttribute("list", listArray);
		return "mid/Pform2";
	}

	@RequestMapping("/data")
	public String pictable() {
		return "WEB-INF/blockchain/datatable";
	}

}
