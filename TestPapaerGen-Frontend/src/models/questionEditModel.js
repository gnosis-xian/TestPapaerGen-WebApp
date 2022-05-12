import * as requestService from '../services/requestServices';
import { checkCode, isArray } from '../utils/myUtils';
import { message } from 'antd';

export default {
  namespace: 'questionEdit',
  state: {
    allQuestionLabels: [],
    chapter1: [],
    chapter2: [],
    label1: [],
    label2: [],
    editTarget: [],
  },
  reducers: {
    allQuestionLabels(state, {payload}) {
      return { ...state, allQuestionLabels: payload}
    },
    chapter1(state, {payload}) {
      return { ...state, chapter1: payload}
    },
    chapter2(state, {payload}) {
      return { ...state, chapter2: payload}
    },
    label1(state, {payload}) {
      return { ...state, label1: payload}
    },
    label2(state, {payload}) {
      return { ...state, label2: payload}
    },
    editTarget(state, {payload}) {
      return { ...state, editTarget: payload}
    }
  },
  effects: {
    *getAllQuestionLabels({payload}, {call, put}) {
      try {
        const res = yield call(requestService.getAllQuestionLabels);
        if(checkCode(res) && isArray(res.data)) {
          let chapter_1 = [];
          res.data.forEach(each => {
            if (chapter_1.indexOf(each.chapter_1) === -1) chapter_1.push(each.chapter_1);
          });
          yield put({ type: 'allQuestionLabels', payload: res.data });
          yield put({type: 'chapter1', payload: chapter_1});
        }
      } catch (e) {
        console.log(e)
      }
    },
    filterChapter2ByChapter1: function* ({payload}, {select, put}) {
      let tmpArray = [];
      const allQuestionLabels = yield select(state => state.questionEdit.allQuestionLabels);
      allQuestionLabels.forEach(each => {
        if(each.chapter_1 === payload && tmpArray.indexOf(each.chapter_2) === -1) tmpArray.push(each.chapter_2);
      });
      yield put({type: 'chapter2', payload: tmpArray});
    },
    filterLabel1ByChapter1: function* ({payload}, {select, put}) {
      let tmpArray = [];
      const allQuestionLabels = yield select(state => state.questionEdit.allQuestionLabels);
      allQuestionLabels.forEach(each => {
        if(each.chapter_1 === payload && tmpArray.indexOf(each.label_1) === -1) tmpArray.push(each.label_1);
      });
      yield put({type: 'label1', payload: tmpArray});
    },
    filterLabel2ByChapter1: function* ({payload}, {select, put}) {
      let tmpArray = [];
      const allQuestionLabels = yield select(state => state.questionEdit.allQuestionLabels);
      allQuestionLabels.forEach(each => {
        if(each.chapter_1 === payload && tmpArray.indexOf(each.label_2) === -1) tmpArray.push(each.label_2);
      });
      yield put({type: 'label2', payload: tmpArray});
    },
    insertSingleQuestionBank: function* ({payload}, {call, put}) {
      const res = yield call(requestService.insertSingleQuestionBank, payload);
      message.success("新增题目成功", 1);
    },
    *getQuestionBankById({payload}, {call, put}) {
      try {
        const res = yield call(requestService.getQuestionBankById, payload);
        if (checkCode(res) && isArray(res.data)) {
          yield put({type: 'editTarget', payload: res.data});
          message.success("获取要编辑的题目信息成功", 1)
        }
      } catch (e) {
        console.log(e)
      }
    },
    *updateQuestionBankById({payload}, {call, put}) {
      try {
        const res = yield call(requestService.updateQuestionBankById, payload);
        if(checkCode(res)) {
          message.success("更新题目信息成功", 1);
        }
      } catch (e) {
        console.log(e)
      }
    },

    *uploadFile({payload}, {call, put}) {
      try {
        const res = yield call(requestService.uploadFile, payload);
        if (checkCode(res)) {
          message.success(`成功导入${res.data.insertCount}条，删除了原有的${res.data.deleteCount}条.`, 3);
        }
      } catch (e) {
        console.log(e)
      }
    },

  },
  subscriptions: {}
}
