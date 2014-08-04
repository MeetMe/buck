/*
 * Copyright 2014-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.rules;

import com.facebook.buck.cli.BuckConfig;
import com.facebook.buck.cli.FakeBuckConfig;
import com.facebook.buck.testutil.FakeProjectFilesystem;
import com.facebook.buck.util.AndroidDirectoryResolver;
import com.facebook.buck.util.FakeAndroidDirectoryResolver;
import com.facebook.buck.util.ProjectFilesystem;

public class TestRepositoryBuilder {
  private ProjectFilesystem filesystem;
  private KnownBuildRuleTypes buildRuleTypes;
  private BuckConfig buckConfig;
  private AndroidDirectoryResolver androidDirectoryResolver;

  public TestRepositoryBuilder() {
    filesystem = new FakeProjectFilesystem();
    buildRuleTypes = DefaultKnownBuildRuleTypes.getDefaultKnownBuildRuleTypes(filesystem);
    buckConfig = new FakeBuckConfig();
    androidDirectoryResolver = new FakeAndroidDirectoryResolver();
  }

  public TestRepositoryBuilder setFilesystem(ProjectFilesystem filesystem) {
    this.filesystem = filesystem;
    return this;
  }

  public TestRepositoryBuilder setBuildRuleTypes(KnownBuildRuleTypes buildRuleTypes) {
    this.buildRuleTypes = buildRuleTypes;
    return this;
  }

  public TestRepositoryBuilder setBuckConfig(BuckConfig buckConfig) {
    this.buckConfig = buckConfig;
    return this;
  }

  public Repository build() {
    return new Repository(
        filesystem,
        buildRuleTypes,
        buckConfig,
        androidDirectoryResolver);
  }
}