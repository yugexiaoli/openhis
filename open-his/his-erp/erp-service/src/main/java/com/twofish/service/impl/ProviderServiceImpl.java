package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.constants.Constants;
import com.twofish.domain.Provider;
import com.twofish.dto.ProviderDto;
import com.twofish.mapper.ProviderMapper;
import com.twofish.service.ProviderService;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Service(methods = {@Method(name = "addProvider",retries = 0)})
public class ProviderServiceImpl  implements ProviderService{
    @Resource
    private ProviderMapper providerMapper;

    @Override
    public DataGridView listProviderForPage(ProviderDto providerDto) {
        Page<Provider> page = new Page<>(providerDto.getPageNum(),providerDto.getPageSize());
        QueryWrapper<Provider> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(providerDto.getProviderName()),Provider.COL_PROVIDER_NAME,providerDto.getProviderName());
        qw.like(StringUtils.isNotBlank(providerDto.getContactName()),Provider.COL_CONTACT_NAME,providerDto.getContactName());
        qw.and(StringUtils.isNotBlank(providerDto.getContactTel()),new Consumer<QueryWrapper<Provider>>() {
            @Override  //效果等同于：tel like ? or mobile like tel
            public void accept(QueryWrapper<Provider> providerQueryWrapper) {
                providerQueryWrapper.like(Provider.COL_CONTACT_TEL,providerDto.getContactTel())
                        .or().like(Provider.COL_CONTACT_MOBILE,providerDto.getContactTel());
            }
        });
        qw.eq(null!=providerDto.getStatus(),Provider.COL_STATUS,providerDto.getStatus());
        providerMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addProvider(ProviderDto providerDto) {
        Provider provider = new Provider();
        BeanUtil.copyProperties(providerDto,provider);
        //设置创建人
        provider.setCreateBy(providerDto.getSimpleUser().getUserName());
        return providerMapper.insert(provider);
    }

    @Override
    public Provider getProviderById(Long providerId) {
        return this.providerMapper.selectById(providerId);
    }

    @Override
    public int updateProvider(ProviderDto providerDto) {
        Provider provider = new Provider();
        BeanUtil.copyProperties(providerDto,provider);
        //设置修改人
        provider.setUpdateBy(providerDto.getSimpleUser().getUserName());
        return providerMapper.updateById(provider);
    }

    @Override
    public int deleteProviderByIds(Long[] providerIds) {
        List<Long> ids = Arrays.asList(providerIds);
        if(ids!=null && ids.size()>0){
            return this.providerMapper.deleteBatchIds(ids);
        }
        return -1;
    }

    @Override
    public DataGridView selectAllProvider() {
        QueryWrapper<Provider> qw = new QueryWrapper<>();
        qw.eq(Provider.COL_STATUS, Constants.STATUS_TRUE);
        return new DataGridView(null,this.providerMapper.selectList(qw));
    }
}
