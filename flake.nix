{
  description = "Controller support for Minecraft Java";

  inputs.flake-parts.inputs.nixpkgs-lib.follows = "nixpkgs";
  inputs.flake-parts.url = "github:hercules-ci/flake-parts";
  inputs.treefmt-nix.inputs.nixpkgs.follows = "nixpkgs";
  inputs.treefmt-nix.url = "github:numtide/treefmt-nix";
  inputs.systems.flake = false;
  inputs.systems.url = "github:nix-systems/default";
  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

  outputs =
    inputs:
    inputs.flake-parts.lib.mkFlake { inherit inputs; } (
      { lib, ... }:
      {
        imports = [
          inputs.treefmt-nix.flakeModule
        ];
        config.systems = import inputs.systems;
        config.perSystem =
          {
            config,
            pkgs,
            system,
            ...
          }:
          {
            config._module.args.pkgs = import inputs.nixpkgs {
              inherit system;
              config.allowUnfree = false;
              overlays = [ ];
            };
            config.devShells.default = pkgs.callPackage (
              {
                addDriverRunpath,
                alsa-lib,
                flite,
                glfw3-minecraft,
                jdk21,
                lib,
                libGL,
                libX11,
                libXcursor,
                libXext,
                libXrandr,
                libXxf86vm,
                libjack2,
                libpulseaudio,
                libusb1,
                mkShell,
                openal,
                pciutils,
                pipewire,
                stdenv,
                udev,
                vulkan-loader,
                xrandr,
                ...
              }:
              mkShell.override { inherit stdenv; } {
                inputsFrom = [
                  config.treefmt.build.devShell
                ];
                nativeBuildInputs = [
                  jdk21
                ];
                buildInputs = [
                  pciutils
                  xrandr
                ];
                ${if stdenv.hostPlatform.isLinux then "LD_LIBRARY_PATH" else null} = lib.makeLibraryPath [
                  addDriverRunpath.driverLink

                  ## native versions
                  glfw3-minecraft
                  openal

                  ## openal
                  alsa-lib
                  libjack2
                  libpulseaudio
                  pipewire

                  ## glfw
                  libGL
                  libX11
                  libXcursor
                  libXext
                  libXrandr
                  libXxf86vm

                  udev # oshi

                  vulkan-loader # VulkanMod's lwjgl

                  flite

                  libusb1
                ];
              }
            ) { };
            config.legacyPackages.nixpkgs = lib.dontRecurseIntoAttrs pkgs;
            config.treefmt = {
              config.programs.nixfmt.enable = true;
              config.projectRootFile = ".github/README.md";
            };
          };
      }
    );
}
