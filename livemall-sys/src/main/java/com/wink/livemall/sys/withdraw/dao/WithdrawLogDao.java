package com.wink.livemall.sys.withdraw.dao;
import com.wink.livemall.sys.withdraw.dto.WithdrawLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;

public interface WithdrawLogDao extends tk.mybatis.mapper.common.Mapper<WithdrawLog>{

    /**
     * 查询该用户所有记录
     * @param user_id
     * @return
     */
    @Select("SELECT * FROM withdraw_log WHERE user_id = #{user_id} ORDER BY create_time desc ")
    List<WithdrawLog> findListByUserId(@Param("user_id")int user_id);

    @SelectProvider(type = sqlProvider.class, method = "insertWithdrawLog")
    void insertWithdrawLog(WithdrawLog withdrawLog,String newDate);

    /**
     * 查询所有根据状态的记录
     * @param state
     * @return
     */
    @Select("SELECT * FROM withdraw_log WHERE state = #{state} ORDER BY create_time desc ")
    List<WithdrawLog> findListByState(@Param("state")int state);


    /**
     * 查询未到账的记录
     * @param state
     * @return
     */
    @Select("SELECT * FROM withdraw_log WHERE state = #{state} and user_id = #{user_id} ORDER BY create_time desc ")
    List<WithdrawLog> findListByStateAndUserId(@Param("state")int state,@Param("user_id")int user_id);

    /**
     * 查询所有根据状态的记录
     * @param merId
     * @return
     */
    @Select("SELECT * FROM withdraw_log WHERE account_log_id = #{merId}  ORDER BY create_time desc ")
    List<WithdrawLog> findListByLogId(@Param("merId")int merId);



    class sqlProvider {
        public String insertWithdrawLog(WithdrawLog withdrawLog,String newDate) {
            String sql = "INSERT INTO withdraw_log  ( account_log_id,phone,partner_trade_no,user_id,state," +
                    "enc_bank_no,enc_true_name,amount,bank_code,`desc`,id_card,create_time,update_time ) "+
            "VALUES " ;
            sql+="('"+ withdrawLog.getAccount_log_id()+"',";
            sql+="'"+ withdrawLog.getPhone()+"',";
            sql+="'"+ withdrawLog.getPartner_trade_no()+"',";
            sql+="'"+ withdrawLog.getUser_id()+"',";
            sql+="'"+ withdrawLog.getState()+"',";
            sql+="'"+ withdrawLog.getEnc_bank_no()+"',";
            sql+="'"+ withdrawLog.getEnc_true_name()+"',";
            sql+="'"+ withdrawLog.getAmount()+"',";
            sql+="'"+ withdrawLog.getBank_code()+"',";
            sql+="'"+ withdrawLog.getDesc()+"',";
            sql+="'"+ withdrawLog.getId_card()+"',";
            sql+="'"+ newDate+"',";
            sql+="'"+ newDate+"')";
            return sql;
        }
    }
}
