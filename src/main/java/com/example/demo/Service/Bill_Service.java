package com.example.demo.Service;

import com.example.demo.Model.Bill;
import com.example.demo.Repository.Bill_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class Bill_Service {
    @Autowired
    Bill_Repository billRepository;

    public List<Bill> getAllBills(){
        return billRepository.findAll();
    }

    public Bill getBillById(long id){
        Optional<Bill> bill = billRepository.findById(id);
        if(bill.isPresent()){
            return bill.get();
        } else{
            throw new EntityNotFoundException();
        }
    }

    public Bill createBill(Bill bill){
        return billRepository.save(bill);
    }

    public Bill updateBill(Long id, Bill bill){
        Optional<Bill> optionalBill = billRepository.findById(id);
        if(optionalBill.isPresent()){
            Bill existingBill = optionalBill.get();
            existingBill.setOrder(bill.getOrder());
            existingBill.setInvoiceNumber(bill.getInvoiceNumber());
            existingBill.setPaymentDate(bill.getPaymentDate());
            existingBill.setPaymentMethod(bill.getPaymentMethod());
            return billRepository.save(existingBill);
        } else{
            throw new EntityNotFoundException();
        }
    }

    public boolean deleteBill(Long id){
        Optional<Bill> optionalBill = billRepository.findById(id);
        if(optionalBill.isPresent()){
            billRepository.deleteById(id);
            return true;
        } else{
            throw new EntityNotFoundException();
        }
    }
}
