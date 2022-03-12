{{/*
Expand the name of the chart.
*/}}
{{- define "dmitrii-popkov-spring-exercise.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "dmitrii-popkov-spring-exercise.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "dmitrii-popkov-spring-exercise.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "dmitrii-popkov-spring-exercise.labels" -}}
helm.sh/chart: {{ include "dmitrii-popkov-spring-exercise.chart" . }}
{{ include "dmitrii-popkov-spring-exercise.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "dmitrii-popkov-spring-exercise.selectorLabels" -}}
app.kubernetes.io/name: {{ include "dmitrii-popkov-spring-exercise.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "dmitrii-popkov-spring-exercise.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "dmitrii-popkov-spring-exercise.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Return the appropriate apiVersion for ingress
*/}}
{{- define "dmitrii-popkov-spring-exercise.ingress.apiVersion" -}}
  {{- if and (.Capabilities.APIVersions.Has "networking.k8s.io/v1") (semverCompare ">= 1.19-0" .Capabilities.KubeVersion.Version) -}}
      {{- print "networking.k8s.io/v1" -}}
  {{- else if .Capabilities.APIVersions.Has "networking.k8s.io/v1beta1" -}}
    {{- print "networking.k8s.io/v1beta1" -}}
  {{- else -}}
    {{- print "extensions/v1beta1" -}}
  {{- end -}}
{{- end -}}

{{/*
Return if ingress is stable.
*/}}
{{- define "dmitrii-popkov-spring-exercise.ingress.isStable" -}}
  {{- eq (include "dmitrii-popkov-spring-exercise.ingress.apiVersion" .) "networking.k8s.io/v1" -}}
{{- end -}}

{{/*
Return if ingress supports ingressClassName.
*/}}
{{- define "dmitrii-popkov-spring-exercise.ingress.supportsIngressClassName" -}}
  {{- or (eq (include "dmitrii-popkov-spring-exercise.ingress.isStable" .) "true") (and (eq (include "dmitrii-popkov-spring-exercise.ingress.apiVersion" .) "networking.k8s.io/v1beta1") (semverCompare ">= 1.18-0" .Capabilities.KubeVersion.Version)) -}}
{{- end -}}

{{/*
Return if ingress supports pathType.
*/}}
{{- define "dmitrii-popkov-spring-exercise.ingress.supportsPathType" -}}
  {{- or (eq (include "dmitrii-popkov-spring-exercise.ingress.isStable" .) "true") (and (eq (include "dmitrii-popkov-spring-exercise.ingress.apiVersion" .) "networking.k8s.io/v1beta1") (semverCompare ">= 1.18-0" .Capabilities.KubeVersion.Version)) -}}
{{- end -}}