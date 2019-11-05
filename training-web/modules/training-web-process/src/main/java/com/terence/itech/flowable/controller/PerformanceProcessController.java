package com.terence.itech.flowable.controller;

import com.terence.itech.base.errorcode.ErrorCodeEnum;
import com.terence.itech.base.exception.GateException;
import com.terence.itech.base.exception.helper.ParamValidateUtil;
import com.terence.itech.base.exception.helper.entity.ParamMap;
import com.terence.itech.base.result.BasePageData;
import com.terence.itech.base.result.BaseResult;
import com.terence.itech.common.controller.BaseController;
import com.terence.itech.flowable.service.CommonFlowableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>绩效流程</p>
 *
 * @author Terence
 * @date 2019/10/25 16:29
 */
@RestController
@RequestMapping("/performance-process")
public class PerformanceProcessController extends BaseController {
  @Autowired
  private CommonFlowableService processService;


  /**
   * 获取待办或已办任务列表
   * listType:待办todo,已办done
   *
   * @author Terence
   * @date 2019/10/28 10:48
   */
  @PostMapping("/process-list")
  @GateException(error = ErrorCodeEnum.PROCESS_TASK_LIST_QUERY_FAILED)
  public BaseResult queryToDoList(Integer pageNo, Integer pageSize, String type, String listType) {
    ParamValidateUtil.validParamsExist(
            ParamMap.getParamMap()
                    .build("pageNo", pageNo)
                    .build("pageSize", pageSize)
                    .build("listType", listType));
    ParamValidateUtil.validParamsExist(ParamMap.getParamMap().build("listType", listType));
    BasePageData res=processService.getProcessList(pageNo, pageSize, getUserId(), type, listType);
    return BaseResult.success(res);
  }


  /**
   * 获取下一步所有分支结点信息
   *
   * @author Terence
   * @date 2019/10/28 11:37
   */
  @PostMapping("/query-next-step")
  @GateException(error = ErrorCodeEnum.PROCESS_NEXT_INFO_ERROR)
  public BaseResult queryNextNodes(String processDefId,String taskId) {
    return BaseResult.success(processService.getNextStepInfo(processDefId,taskId));
  }

  /**
   * 根据taskId直接推到下一步
   * 自动携带参数
   * nextHandler格式：assignee:userId
   * @author Terence
   * @date 2019/10/28 11:29
   */
  @PostMapping("/to-next")
  @GateException(error = ErrorCodeEnum.PROCESS_TASK_COMPLETE_FAILED)
  public BaseResult complete(String taskId, String advice,String flowCondition,String nextAssignee) {
    processService.complete2next(taskId,flowCondition,advice,nextAssignee);
    return BaseResult.success();
  }

  @PostMapping("/task-list-detail")
  @GateException(error = ErrorCodeEnum.PROCESS_TASK_COMPLETE_FAILED)
  public BaseResult queryTaskList(String processDefId,String processId) {
    ParamValidateUtil.validParamsExist(
            ParamMap.getParamMap()
                    .build("processDefId",processDefId)
                    .build("processId",processId));
    List resList=processService.getTaskNodeList(processDefId,processId);
    return BaseResult.success(resList);
  }
}