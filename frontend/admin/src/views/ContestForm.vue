<script setup>
import { message } from "ant-design-vue";
import { reactive } from "vue";
import { useCommonStore, useContestStore } from "../plugins/pinia";
import Apis from "../utils/apis";
import { goContestItem } from "../utils/router-helper";
import { getDurationTime } from "../utils/utils";

const commonStore = useCommonStore();
const contestStore = useContestStore();

const state = reactive({
  title: null,
  type: null,
  description: null,
  duration: null,
  startTime: null,
});

const durationMark = {
  60: "1h",
  120: "2h",
  180: "3h",
  240: "4h",
  300: "5h",
};

function onFinish() {
  const form = {
    title: state.title,
    type: state.type,
    description: state.description,
    duration: state.duration,
    startTime: state.startTime.format("YYYY-MM-DD HH:mm:ss"),
  };
  Apis.ContestModule.newContest(form).then((body) => {
    if (body.status) {
      contestStore.refreshData();
      message.success("Contest Create Success");
      goContestItem(body.data);
    }
  });
}
</script>
<template>
  <a-form
    :model="state"
    class="contest-form"
    :label-col="{ style: { width: '150px' } }"
    :wrapper-col="{ span: 14 }"
    @finish="onFinish"
  >
    <a-form-item
      label="Title"
      name="title"
      :rules="[{ required: true, message: 'Please input contest title!' }]"
    >
      <a-input v-model:value="state.title" />
    </a-form-item>
    <a-form-item
      label="Type"
      name="type"
      :rules="[{ required: true, message: 'Please select contest type!' }]"
    >
      <a-radio-group v-model:value="state.type">
        <a-radio v-for="cType in commonStore.contestTypes" :value="cType.id">{{
          cType.text
        }}</a-radio>
      </a-radio-group>
    </a-form-item>
    <a-form-item
      label="Description"
      name="description"
      :rules="[{ required: true, message: 'Please describe this contest!' }]"
    >
      <a-textarea v-model:value="state.description" :rows="8" />
    </a-form-item>
    <a-form-item
      label="Start Time"
      name="startTime"
      :rules="[{ required: true, message: 'Please select the start time!' }]"
    >
      <a-date-picker
        :show-time="{ format: 'HH:mm' }"
        :format="'YYYY/MM/DD HH:mm'"
        placeholder="Select Time"
        v-model:value="state.startTime"
      />
    </a-form-item>
    <a-form-item
      label="Duration"
      name="duration"
      :rules="[
        { required: true, message: 'Please set the duration time for this contest!' },
      ]"
    >
      <a-row type="flex" justify="space-around" align="middle">
        <a-col :span="18">
          <a-slider v-model:value="state.duration" :marks="durationMark" :max="300" />
        </a-col>
        <a-col :span="2">
          <a-tag color="cyan">{{ getDurationTime(state.duration) }}</a-tag>
        </a-col>
      </a-row>
    </a-form-item>
    <a-form-item style="text-align: center">
      <a-button type="primary" html-type="submit">Push New Contest!</a-button>
    </a-form-item>
  </a-form>
</template>

<style lang="scss" scoped>
.contest-form {
  margin-top: 50px;
}
</style>
