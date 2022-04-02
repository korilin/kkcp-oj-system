<script setup>
import { reactive } from 'vue';

const state = reactive({
  title: null,
  type: null,
  description: null,
  duration: null,
  startTime: null
})

const types = [
  { id: 0, text: "hi1" },
  { id: 1, text: "hi2" }
]

const durationMark = {
  60: '1h',
  120: '2h',
  180: '3h',
  240: '4h',
  300: '5h',
}

function getDurationTime() {
  if (state.duration == null) return "0h 0min"
  let h = parseInt(state.duration / 60)
  let min = state.duration - h * 60
  return `${h}h ${min}min`
}
</script>
<template>
  <a-form :model="state" class="contest-form">
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
        <a-radio v-for="cType in types" :value="cType.id">{{ cType.text }}</a-radio>
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
      <a-date-picker show-time placeholder="Select Time" v-model:value="state.startTime" />
    </a-form-item>
    <a-form-item
      label="Duration"
      name="duration"
      :rules="[{ required: true, message: 'Please set the duration time for this contest!' }]"
    >
      <a-row type="flex" justify="space-around" align="middle">
        <a-col :span="18">
          <a-slider v-model:value="state.duration" :marks="durationMark" :max="300" />
        </a-col>
        <a-col :span="2">
          <a-tag color="cyan">{{ getDurationTime() }}</a-tag>
        </a-col>
      </a-row>
    </a-form-item>
    <a-form-item style="text-align: center;">
      <a-button type="primary" html-type="submit">Push New Contest!</a-button>
    </a-form-item>
  </a-form>
</template>

<style lang="scss" scoped>
.contest-form {
  width: 600px;
}
</style>
