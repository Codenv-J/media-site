package com.coden.initial;

import com.coden.service.IUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName InitCommandLineRunner
 * @Description TODO
 **/
@Component
public class InitCommandLineRunner implements CommandLineRunner {


    @Resource
    IUserService userService;

    /**
     * @Description 系统启动时，执行初始化操作
     * @Param [args]
     * @return void
     **/
    @Override
    public void run(String... args) throws Exception {
        userService.initFirstUser();
    }
}
