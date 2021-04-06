package com.wink.livemall.sys.code.dao;

import com.wink.livemall.sys.code.dto.LmSmsVcode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Mapper
public interface LmSmsVcodeDao extends tk.mybatis.mapper.common.Mapper<LmSmsVcode>{

    @Select("SELECT * FROM lm_sms_vcode WHERE mobile = #{mobile} order by creatdate desc")
    List<LmSmsVcode> findByMobile(@Param("mobile") String mobile);

    @SelectProvider(type = sqlProvider.class, method = "restrictMobile")
    List<LmSmsVcode> restrictMobile(@Param("mobile") String mobile);



    class sqlProvider {

        public String restrictMobile(@Param("mobile") String mobile) {
            StringBuilder sql = new StringBuilder();
            String startTime = calc_date(0);
			String endTime = calc_date(1);
            sql.append(" SELECT * FROM lm_sms_vcode WHERE mobile = #{mobile} " );
            sql.append(" and creatdate between '" + startTime + "'" + " and " + "'" + endTime + "'");
            System.out.println(sql.toString());
            return sql.toString();
        }
        private String calc_date(int day) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.DAY_OF_MONTH, day);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            return sf.format(c1.getTime());
        }
    }

}
