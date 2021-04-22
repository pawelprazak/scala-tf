#!/bin/sh -e
latest=$(curl -LSs "https://releases.hashicorp.com/terraform-provider-$1/" 2> /dev/null \
  | grep -m1 -o "terraform-provider-${1}_[0-9.]*" \
  | cut -d_ -f2)
echo "https://releases.hashicorp.com/terraform-provider-$1/${latest}"
curl -LSs "https://releases.hashicorp.com/terraform-provider-$1/${latest}/terraform-provider-$1_${latest}_darwin_amd64.zip" -O
unzip terraform-provider-$1_${latest}_darwin_amd64.zip
ls terraform-provider-$1_${latest}*
echo "You can start the plugin with TF_PLUGIN_MAGIC_COOKIE, e.g:"
echo "  export TF_PLUGIN_MAGIC_COOKIE=\"d602bf8f470bc67ca7faa0386276bbdd4330efaf76d1a219cb4d6991ca9872b2\";./terraform-provider-$1_${latest}_x5"