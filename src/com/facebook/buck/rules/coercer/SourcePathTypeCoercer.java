/*
 * Copyright 2013-present Facebook, Inc.
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

package com.facebook.buck.rules.coercer;

import com.facebook.buck.model.BuildTarget;
import com.facebook.buck.parser.BuildTargetParser;
import com.facebook.buck.rules.BuildRule;
import com.facebook.buck.rules.BuildRuleResolver;
import com.facebook.buck.rules.BuildRuleSourcePath;
import com.facebook.buck.rules.PathSourcePath;
import com.facebook.buck.rules.SourcePath;
import com.facebook.buck.util.ProjectFilesystem;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import java.nio.file.Path;

public class SourcePathTypeCoercer extends LeafTypeCoercer<SourcePath> {
  private final TypeCoercer<BuildTarget> buildTargetTypeCoercer;
  private final TypeCoercer<Path> pathTypeCoercer;

  SourcePathTypeCoercer(
      TypeCoercer<BuildTarget> buildTargetTypeCoercer,
      TypeCoercer<Path> pathTypeCoercer) {
    this.buildTargetTypeCoercer = Preconditions.checkNotNull(buildTargetTypeCoercer);
    this.pathTypeCoercer = Preconditions.checkNotNull(pathTypeCoercer);
  }

  @Override
  public Class<SourcePath> getOutputClass() {
    return SourcePath.class;
  }

  @Override
  public SourcePath coerce(
      BuildTargetParser buildTargetParser,
      BuildRuleResolver buildRuleResolver,
      ProjectFilesystem filesystem,
      Path pathRelativeToProjectRoot,
      Object object)
      throws CoerceFailedException {
    if ((object instanceof String) && (
        ((String) object).startsWith("//") || ((String) object).startsWith(":"))
        ) {
      BuildTarget buildTarget =
          buildTargetTypeCoercer.coerce(
              buildTargetParser,
              buildRuleResolver,
              filesystem,
              pathRelativeToProjectRoot,
              object);
      Optional<BuildRule> rule = buildRuleResolver.getRuleOptional(buildTarget);

      if (!rule.isPresent()) {
        throw CoerceFailedException.simple(object, BuildRule.class);
      }
      return new BuildRuleSourcePath(rule.get());
    } else {
      Path path = pathTypeCoercer.coerce(
          buildTargetParser,
          buildRuleResolver,
          filesystem,
          pathRelativeToProjectRoot,
          object);
      return new PathSourcePath(path);
    }
  }
}
