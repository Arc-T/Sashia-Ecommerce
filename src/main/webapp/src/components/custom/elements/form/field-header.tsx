import { type ReactNode } from "react";
import { useTranslation } from "react-i18next";

import { FieldDescription, FieldLabel } from "@/components/ui/field";

interface FieldHeaderProps {
  label?: string;
  description?: string;
  icon?: ReactNode;
  required?: boolean;
}

export function FieldHeader({
  label,
  description,
  icon,
  required = false,
}: FieldHeaderProps) {
  const { t, i18n } = useTranslation();

  if (!label && !description) {
    return null;
  }

  return (
    <>
      {label && (
        <FieldLabel dir={i18n.dir()} className="flex items-center gap-2">
          {icon}

          <span>{t(label)}</span>

          {required && (
            <span aria-label={t("required")} className="text-destructive">
              *
            </span>
          )}
        </FieldLabel>
      )}

      {description && (
        <FieldDescription dir={i18n.dir()}>{t(description)}</FieldDescription>
      )}
    </>
  );
}
