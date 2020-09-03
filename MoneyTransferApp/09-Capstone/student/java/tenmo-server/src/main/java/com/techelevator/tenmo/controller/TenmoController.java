package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@PreAuthorize("isAuthenticated()")
@RestController
public class TenmoController {
	private TransferDAO transferDao;
	private AccountDAO accountDao;
	private UserDAO userDao;

	public TenmoController(TransferDAO transferDao, AccountDAO accountDao, UserDAO userDao) {
		this.transferDao = transferDao;
		this.accountDao = accountDao;
		this.userDao = userDao;
	}

	@RequestMapping(path = "account/{id}/balance", method = RequestMethod.GET)
	public BigDecimal getAccountBalance(@PathVariable long id) {
		return accountDao.getAccountBalance(id);
	}

	@RequestMapping(path = "account/sendtransfer", method = RequestMethod.POST)
	public void sendTransfer(@RequestBody Transfer transfer) {

		if (accountDao.getAccountBalance(transfer.getAccountFrom()).compareTo(transfer.getAmount()) == 1) {
			transferDao.sendTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
			accountDao.updateSenderBalance(transfer.getAccountFrom(), transfer.getAmount());
			accountDao.updateReceiverBalance(transfer.getAccountTo(), transfer.getAmount());
		} else {
			System.out.println("Insufficient funds for this transfer.");
		}
	}
	
	@RequestMapping(path = "account/{userId}/transfer/viewtransfer/{transferId}")
	public Transfer getTransferById(@PathVariable long userId, @PathVariable long transferId) {
		return transferDao.getTransferById(transferId);
	}
	
	@RequestMapping(path = "account/{id}/transfer", method = RequestMethod.GET)
	public Transfer[] getAllTransfers(@PathVariable long id) {
		List<Transfer> transferList = new ArrayList<>();
		transferList = transferDao.getAllTransfersByUser(id);
		Transfer[] transferArray = new Transfer[transferList.size()];
		for (int i = 0; i < transferArray.length; i++) {
			transferArray[i] = transferList.get(i);
		}
		return transferArray;
	}

	@RequestMapping(path = "users", method = RequestMethod.GET)
	public List<User> getAllUsers() {
		List<User> userList = userDao.findAll();
		return userList;
	}
	@RequestMapping(path = "users/{id}", method = RequestMethod.GET)
	public User findUserById(@PathVariable long id) {
		User theUser = userDao.findUserById(id);
		return theUser;
	}
}
